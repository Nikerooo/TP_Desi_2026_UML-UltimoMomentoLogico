// A cargo de: Yamila Esteban - 29100683

package tuti.desi.servicios;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.enums.EstadoPublicacion;
import tuti.desi.persistencia.PropiedadPersistencia;
import tuti.desi.persistencia.PublicacionPersistencia;

// Servicio con la lógica de negocio del Epic 2 
// @Transactional asegura que si algo falla, los cambios en la BD se revierten

@Service
@Transactional
public class PublicacionServicios {

    @Autowired
    private PublicacionPersistencia publicacionRepo;

    @Autowired
    private PropiedadPersistencia propiedadRepo;

    // 2.4 - Listado: devuelve todas las publicaciones no eliminadas
    public List<Publicacion> listarTodas() {
        return publicacionRepo.findByEliminadoFalse();
    }


    // epic 2.1 - Alta de publicación
    public Publicacion crear(Publicacion publicacion) {

        // Verificar que la propiedad existe en la BD
        Propiedad propiedad = propiedadRepo
                .findById(publicacion.getPropiedad().getId())
                .orElseThrow(() -> new IllegalArgumentException("La propiedad no existe."));

        // Regla de negocio: no se puede publicar una propiedad eliminada
        if (propiedad.isEliminada()) {
            throw new IllegalArgumentException("No se puede publicar una propiedad eliminada.");
        }

        // Regla de negocio: la propiedad debe estar DISPONIBLE para poder publicarse
        if (propiedad.getEstadoDisp() != EstadoDisponibilidad.DISPONIBLE) {
            throw new IllegalArgumentException(
                "Solo se puede publicar una propiedad en estado DISPONIBLE.");
        }

        // Regla de negocio: no puede haber dos publicaciones ACTIVAS para la misma propiedad
        if (publicacionRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(
                propiedad.getId(), EstadoPublicacion.ACTIVA)) {
            throw new IllegalArgumentException(
                "Ya existe una publicación activa para esta propiedad.");
        }

        publicacion.setPropiedad(propiedad);
        return publicacionRepo.save(publicacion);
    }

    //epic 2.2 - Eliminación lógica (no se borra de la BD, solo se marca como eliminado)
    public void eliminar(Long id) {

        Publicacion publicacion = obtenerOException(id);

        // Regla de negocio: solo se pueden eliminar publicaciones en estado ACTIVA
        if (publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
            throw new IllegalArgumentException(
                "Solo pueden eliminarse publicaciones en estado ACTIVA.");
        }

        publicacion.setEliminado(true);
        publicacionRepo.save(publicacion);
    }

    // Busca una publicación por ID, o lanza excepción si no existe
    public Publicacion obtenerPorId(Long id) {
        return obtenerOException(id);
    }

    public Publicacion obtenerOException(Long id) {
        return publicacionRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la publicación con id " + id));
    }

    // Métodos auxiliares para cargar los combos del formulario
    public List<Propiedad> obtenerTodasPropiedades() {
        return propiedadRepo.buscarTodasActivas();
    }

    // Solo propiedades DISPONIBLES para el combo de alta (HU 2.1)
    public List<Propiedad> obtenerPropiedadesDisponibles() {
        return propiedadRepo.buscarDisponibles();
    }

    // epic 2.3 - Modificación de publicación
    public Publicacion modificar(Long id, Publicacion datos) {

        Publicacion existente = obtenerOException(id);

        // Si se quiere reactivar la publicación, se vuelven a validar las reglas de negocio
        if (datos.getEstado() == EstadoPublicacion.ACTIVA
                && existente.getEstado() != EstadoPublicacion.ACTIVA) {

            Propiedad propiedad = existente.getPropiedad();

            // La propiedad debe seguir estando DISPONIBLE
            if (propiedad.getEstadoDisp() != EstadoDisponibilidad.DISPONIBLE) {
                throw new IllegalArgumentException(
                    "Solo se puede activar una publicación si la propiedad está DISPONIBLE.");
            }

            // No puede haber ya otra publicación activa para esa propiedad
            if (publicacionRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(
                    propiedad.getId(), EstadoPublicacion.ACTIVA)) {
                throw new IllegalArgumentException(
                    "Ya existe otra publicación activa para esta propiedad.");
            }
        }

        // Regla de negocio: las condiciones no se pueden editar si la publicación está FINALIZADA
        if (existente.getEstado() == EstadoPublicacion.FINALIZADA) {
            if (!datos.getCondiciones().equals(existente.getCondiciones())) {
                throw new IllegalArgumentException(
                    "No se pueden modificar las condiciones de una publicación FINALIZADA.");
            }
        }

        // Se actualizan los campos permitidos
        existente.setPrecioMensual(datos.getPrecioMensual());
        existente.setCondiciones(datos.getCondiciones());
        existente.setDescripcion(datos.getDescripcion());
        existente.setFechaPublicacion(datos.getFechaPublicacion());

        // Se usa cambiarEstado() en lugar de setEstado() para registrar el cambio en el historial
        existente.cambiarEstado(datos.getEstado());

        return publicacionRepo.save(existente);
    }

    // epic 2.4 - Listado con filtros opcionales (se aplican en memoria con stream)
            public List<Publicacion> listarConFiltros(
            Long propiedadId,
            String ciudad,
            EstadoPublicacion estado,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        return publicacionRepo.findByEliminadoFalse()
                .stream()
                // Filtra por ID de propiedad si se seleccionó alguna
                .filter(p -> propiedadId == null
                        || p.getPropiedad().getId().equals(propiedadId))
                // Filtra por ciudad (contiene el texto ingresado, sin distinguir mayúsculas)
                .filter(p -> ciudad == null
                        || ciudad.isBlank()
                        || p.getPropiedad().getCiudad()
                                .toLowerCase()
                                .contains(ciudad.toLowerCase()))
                // Filtra por estado si se seleccionó uno
                .filter(p -> estado == null || p.getEstado() == estado)
                // Filtra por precio mínimo
                .filter(p -> precioMin == null
                        || p.getPrecioMensual().compareTo(precioMin) >= 0)
                // Filtra por precio máximo
                .filter(p -> precioMax == null
                        || p.getPrecioMensual().compareTo(precioMax) <= 0)
                .collect(Collectors.toList());
    
            	}
}

