package tuti.desi.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.enums.TipoPropiedad;
import tuti.desi.persistencia.PersonaPersistencia;
import tuti.desi.persistencia.PropiedadPersistencia;
import tuti.desi.persistencia.contratoPersistencia;

import java.util.List;

@Service
@Transactional
public class PropiedadServicio {

    @Autowired
    private PropiedadPersistencia propiedadRepo;

    @Autowired
    private PersonaPersistencia personaRepo;

    /*
     * TODO (Epic 3 - Contratos): cuando contratoPersistencia esté disponible,
     * descomentar la siguiente inyección y los bloques marcados con "HU 1.2" y "HU 1.3".
     * Ya está importada la clase; si no compila porque otro compañero no subió aún
     * contratoPersistencia, coménta el @Autowired y los bloques marcados.
     */
    @Autowired
    private ContratoPersistencia contratoRepo;

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.1 - Alta de Propiedad
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Crea una nueva propiedad aplicando todas las validaciones de HU 1.1.
     *
     * @param propiedad    Objeto con los datos del formulario (sin id ni propietario completo).
     * @param propietarioId Id de la Persona propietaria seleccionada en el combo.
     */
    public Propiedad crear(Propiedad propiedad, Long propietarioId) {

        // 1. Validaciones de campos numéricos (la validación HTML puede saltarse por Postman, etc.)
        validarCamposNumericos(propiedad);

        // 2. El propietario debe existir y no estar eliminado.
        Persona propietario = obtenerPersonaActivaOException(propietarioId);

        // 3. No puede existir otra propiedad activa con la misma dirección + ciudad.
        if (propiedadRepo.existsByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadaFalse(
                propiedad.getDireccion(), propiedad.getCiudad())) {
            throw new IllegalArgumentException(
                    "Ya existe una propiedad activa en '" + propiedad.getDireccion()
                    + "', " + propiedad.getCiudad() + ". No se permiten duplicados.");
        }

        // 4. Estado por defecto: DISPONIBLE si no se indicó uno.
        if (propiedad.getEstadoDisp() == null) {
            propiedad.setEstadoDisp(EstadoDisponibilidad.DISPONIBLE);
        }

        // 5. Asignar propietario y marcar como activa.
        propiedad.setPropietario(propietario);
        propiedad.setEliminada(false);

        // 6. Primer registro en el historial (requerido por HU 1.1).
        propiedad.cambiarEstado(propiedad.getEstadoDisp());

        return propiedadRepo.save(propiedad);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.2 - Eliminación lógica de Propiedad
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Marca la propiedad como eliminada (baja lógica).
     * No borra la fila de la base de datos ni elimina datos relacionados.
     */
    public void eliminar(Long id) {

        Propiedad propiedad = obtenerActivaOException(id);

        // HU 1.2 - No eliminar si tiene contrato ACTIVO vigente.
        // Si contratoPersistencia no está disponible aún, comentar el bloque siguiente
        // y reemplazarlo por: // TODO: validar contrato activo cuando Epic 3 esté integrado
        List<Contrato> contratosActivos =
                contratoRepo.findByPropiedadIdAndEstado(id, EstadoContrato.ACTIVO);
        if (!contratosActivos.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la propiedad porque tiene un contrato ACTIVO vigente. "
                    + "Finalice o rescindir el contrato antes de eliminar la propiedad.");
        }

        propiedad.setEliminada(true);
        propiedadRepo.save(propiedad);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.3 - Modificación de Propiedad
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Actualiza todos los campos editables de una propiedad.
     * Re-valida todas las reglas de HU 1.1 más las restricciones de HU 1.3.
     */
    public Propiedad modificar(Long id, Propiedad datos, Long propietarioId) {

        Propiedad existente = obtenerActivaOException(id);

        // 1. Validaciones de campos numéricos.
        validarCamposNumericos(datos);

        // 2. El propietario debe existir y no estar eliminado.
        Persona propietario = obtenerPersonaActivaOException(propietarioId);

        // 3. No puede quedar duplicada en dirección + ciudad con OTRA propiedad activa.
        if (propiedadRepo.existsByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadaFalseAndIdNot(
                datos.getDireccion(), datos.getCiudad(), id)) {
            throw new IllegalArgumentException(
                    "Ya existe otra propiedad activa en '" + datos.getDireccion()
                    + "', " + datos.getCiudad() + ". No se permiten duplicados.");
        }

        // 4. HU 1.3 - Si cambia el estado, verificar que no haya contrato activo
        //    antes de pasar a DISPONIBLE o INACTIVA.
        EstadoDisponibilidad estadoAnterior = existente.getEstadoDisp();
        EstadoDisponibilidad estadoNuevo    = datos.getEstadoDisp();
        boolean cambiandoEstado = !estadoAnterior.equals(estadoNuevo);

        if (cambiandoEstado
                && (estadoNuevo == EstadoDisponibilidad.DISPONIBLE
                    || estadoNuevo == EstadoDisponibilidad.INACTIVA)) {

            // Si contratoPersistencia no está disponible aún, comentar el bloque siguiente
            // y reemplazarlo por: // TODO: validar contrato activo cuando Epic 3 esté integrado
            List<Contrato> contratosActivos =
                    contratoRepo.findByPropiedadIdAndEstado(id, EstadoContrato.ACTIVO);
            if (!contratosActivos.isEmpty()) {
                throw new IllegalArgumentException(
                        "No se puede cambiar el estado a " + estadoNuevo.name()
                        + " porque la propiedad tiene un contrato ACTIVO vigente. "
                        + "Finalice o rescindir el contrato primero.");
            }
        }

        // 5. Aplicar todos los campos editables.
        existente.setDireccion(datos.getDireccion());
        existente.setCiudad(datos.getCiudad());
        existente.setTipo(datos.getTipo());
        existente.setCantAmbientes(datos.getCantAmbientes());
        existente.setMtsCuadrados(datos.getMtsCuadrados());
        existente.setDescripcion(datos.getDescripcion());
        existente.setComodidades(datos.getComodidades());
        existente.setPropietario(propietario);

        // 6. Si cambió el estado, registrar en historial (HU 1.3 + HU 1.1 última regla).
        if (cambiandoEstado) {
            existente.cambiarEstado(estadoNuevo);
        }

        return propiedadRepo.save(existente);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.4 - Listado con filtros
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Lista solo propiedades no eliminadas, con filtros opcionales.
     * Cualquier parámetro en null o vacío se ignora (muestra todos).
     */
    @Transactional(readOnly = true)
    public List<Propiedad> listarConFiltros(
            String direccion,
            String ciudad,
            TipoPropiedad tipo,
            EstadoDisponibilidad estado) {

        return propiedadRepo.buscarConFiltros(
                (direccion != null && !direccion.isBlank()) ? direccion : null,
                (ciudad    != null && !ciudad.isBlank())    ? ciudad    : null,
                tipo,
                estado);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Métodos auxiliares
    // ──────────────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Propiedad obtenerPorId(Long id) {
        return obtenerActivaOException(id);
    }

    @Transactional(readOnly = true)
    public List<Persona> obtenerPropietariosActivos() {
        return personaRepo.buscarNoEliminadas();
    }

    // Lanza excepción clara si la propiedad no existe o está eliminada.
    private Propiedad obtenerActivaOException(Long id) {
        Propiedad p = propiedadRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe ninguna propiedad con id " + id + "."));
        if (p.isEliminada()) {
            throw new IllegalArgumentException(
                    "La propiedad con id " + id + " está eliminada del sistema.");
        }
        return p;
    }

    // Lanza excepción clara si la persona no existe o está eliminada.
    private Persona obtenerPersonaActivaOException(Long personaId) {
        Persona persona = personaRepo.findById(personaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El propietario seleccionado no existe en el sistema."));
        if (persona.isEliminado()) {
            throw new IllegalArgumentException(
                    "El propietario seleccionado está dado de baja. Elija uno activo.");
        }
        return persona;
    }

    // Valida que cantAmbientes >= 1 y mtsCuadrados > 0.
    private void validarCamposNumericos(Propiedad p) {
        if (p.getCantAmbientes() < 1) {
            throw new IllegalArgumentException(
                    "La cantidad de ambientes debe ser al menos 1.");
        }
        if (p.getMtsCuadrados() <= 0) {
            throw new IllegalArgumentException(
                    "Los metros cuadrados deben ser un número positivo mayor a 0.");
        }
    }
}
