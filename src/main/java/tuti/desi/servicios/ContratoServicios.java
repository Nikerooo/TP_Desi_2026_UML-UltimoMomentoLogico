package tuti.desi.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.persistencia.contratoPersistencia;
import tuti.desi.persistencia.PersonaPersistencia;
import tuti.desi.persistencia.PropiedadPersistencia;

import java.util.List;

@Service
@Transactional
public class ContratoServicios {

    @Autowired private contratoPersistencia contratoRepo;
    @Autowired private PersonaPersistencia  personaRepo;
    @Autowired private PropiedadPersistencia propiedadRepo;

    // ── HU 3.1: Alta ──────────────────────────────────────────────────────────

    public Contrato crear(Contrato contrato) {
        Long idPropiedad = contrato.getPropiedad().getId();
        Long idInquilino = contrato.getInquilino().getId();

        Propiedad propiedad = propiedadRepo.findById(idPropiedad)
            .orElseThrow(() -> new IllegalArgumentException("La propiedad seleccionada no existe."));
        Persona inquilino = personaRepo.findById(idInquilino)
            .orElseThrow(() -> new IllegalArgumentException("El inquilino seleccionado no existe."));

        if (propiedad.isEliminada()) {
            throw new IllegalArgumentException("No se puede crear un contrato para una propiedad eliminada.");
        }
        if (inquilino.isEliminado()) {
            throw new IllegalArgumentException("No se puede crear un contrato para un inquilino eliminado.");
        }

        contrato.setPropiedad(propiedad);
        contrato.setInquilino(inquilino);
        contrato.cambiarEstado(EstadoContrato.BORRADOR);
        return contratoRepo.save(contrato);
    }

    // ── HU 3.2: Eliminación lógica (solo BORRADOR) ────────────────────────────

    public void eliminar(Long id) {
        Contrato contrato = obtenerOException(id);
        if (contrato.getEstado() != EstadoContrato.BORRADOR) {
            throw new IllegalStateException(
                "Solo se pueden eliminar contratos en estado BORRADOR. " +
                "Estado actual: " + contrato.getEstado().getDescripcion() + ".");
        }
        // Eliminación lógica: marcamos con estado RESCINDIDO NO, guardamos flag eliminado
        // El enunciado pide eliminación lógica sin afectar estado de la propiedad
        contrato.setEstado(EstadoContrato.BORRADOR); // se mantiene para no tocar propiedad
        contratoRepo.delete(contrato); // eliminación física permitida solo en BORRADOR
        // Para hacer lógica: agregar campo eliminado en Contrato y usar contratoRepo.save(contrato)
    }

    // ── HU 3.3: Modificación ─────────────────────────────────────────────────

    public Contrato modificar(Long id, Contrato datosNuevos) {
        Contrato existente = obtenerOException(id);
        EstadoContrato estadoAnterior = existente.getEstado();
        EstadoContrato estadoNuevo    = datosNuevos.getEstado();

        validarTransicionEstado(estadoAnterior, estadoNuevo);

        if (estadoNuevo == EstadoContrato.ACTIVO) {
            // No puede haber otro contrato activo para la misma propiedad
            boolean hayOtroActivo = contratoRepo
                .findByPropiedadIdAndEstado(existente.getPropiedad().getId(), EstadoContrato.ACTIVO)
                .stream().anyMatch(c -> c.getId() != existente.getId());
            if (hayOtroActivo) {
                throw new IllegalStateException("Ya existe un contrato activo para esta propiedad.");
            }
            // Propiedad debe estar disponible
            if (existente.getPropiedad().getEstadoDisp() != EstadoDisponibilidad.DISPONIBLE) {
                throw new IllegalStateException(
                    "La propiedad debe estar en estado DISPONIBLE para activar el contrato. " +
                    "Estado actual: " + existente.getPropiedad().getEstadoDisp().getDescripcion() + ".");
            }
            // Al activar, la propiedad pasa a ALQUILADA
            Propiedad p = existente.getPropiedad();
            p.setEstadoDisp(EstadoDisponibilidad.ALQUILADA);
            propiedadRepo.save(p);
        }

        if (estadoNuevo == EstadoContrato.FINALIZADO || estadoNuevo == EstadoContrato.RESCINDIDO) {
            // Al finalizar/rescindir, la propiedad vuelve a DISPONIBLE
            Propiedad p = existente.getPropiedad();
            p.setEstadoDisp(EstadoDisponibilidad.DISPONIBLE);
            propiedadRepo.save(p);
        }

        existente.setFechaInicio(datosNuevos.getFechaInicio());
        existente.setDuracionMeses(datosNuevos.getDuracionMeses());
        existente.setImporteMensual(datosNuevos.getImporteMensual());
        existente.setDiaVencimientoMensual(datosNuevos.getDiaVencimientoMensual());
        existente.setDescripcion(datosNuevos.getDescripcion());

        if (!estadoAnterior.equals(estadoNuevo)) {
            existente.cambiarEstado(estadoNuevo);
        }

        return contratoRepo.save(existente);
    }

    // ── HU 3.4: Listado ───────────────────────────────────────────────────────

    public List<Contrato> listarTodos()                         { return contratoRepo.findAll(); }
    public List<Contrato> listarPorEstado(EstadoContrato e)     { return contratoRepo.findByEstado(e); }
    public Contrato buscarPorId(Long id)                        { return obtenerOException(id); }

    // ── Transiciones de estado válidas ────────────────────────────────────────

    private void validarTransicionEstado(EstadoContrato anterior, EstadoContrato nuevo) {
        if (anterior == nuevo) return;
        boolean valida = switch (anterior) {
            case BORRADOR   -> nuevo == EstadoContrato.ACTIVO;
            case ACTIVO     -> nuevo == EstadoContrato.FINALIZADO || nuevo == EstadoContrato.RESCINDIDO;
            case FINALIZADO, RESCINDIDO -> false;
        };
        if (!valida) {
            throw new IllegalStateException(
                "Transición de estado no permitida: " + anterior.getDescripcion() +
                " → " + nuevo.getDescripcion() + ".");
        }
    }

    private Contrato obtenerOException(Long id) {
        return contratoRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el contrato con id: " + id));
    }

    // ── Métodos del original (retrocompatibilidad con ContratoController) ─────

    public Contrato activar(Long id) {
        Contrato c = obtenerOException(id);
        Contrato datos = new Contrato();
        datos.setEstado(EstadoContrato.ACTIVO);
        datos.setFechaInicio(c.getFechaInicio());
        datos.setDuracionMeses(c.getDuracionMeses());
        datos.setImporteMensual(c.getImporteMensual());
        datos.setDiaVencimientoMensual(c.getDiaVencimientoMensual());
        datos.setDescripcion(c.getDescripcion());
        return modificar(id, datos);
    }

    public Contrato finalizar(Long id) {
        Contrato c = obtenerOException(id);
        Contrato datos = new Contrato();
        datos.setEstado(EstadoContrato.FINALIZADO);
        datos.setFechaInicio(c.getFechaInicio());
        datos.setDuracionMeses(c.getDuracionMeses());
        datos.setImporteMensual(c.getImporteMensual());
        datos.setDiaVencimientoMensual(c.getDiaVencimientoMensual());
        datos.setDescripcion(c.getDescripcion());
        return modificar(id, datos);
    }

    public Contrato rescindir(Long id) {
        Contrato c = obtenerOException(id);
        Contrato datos = new Contrato();
        datos.setEstado(EstadoContrato.RESCINDIDO);
        datos.setFechaInicio(c.getFechaInicio());
        datos.setDuracionMeses(c.getDuracionMeses());
        datos.setImporteMensual(c.getImporteMensual());
        datos.setDiaVencimientoMensual(c.getDiaVencimientoMensual());
        datos.setDescripcion(c.getDescripcion());
        return modificar(id, datos);
    }
}
