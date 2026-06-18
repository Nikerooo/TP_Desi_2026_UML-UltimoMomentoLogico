package tuti.desi.servicios;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoPublicacion;
import tuti.desi.persistencia.PropiedadPersistencia;
import tuti.desi.persistencia.PublicacionPersistencia;

@Service
@Transactional
public class PublicacionServicios {

    @Autowired
    private PublicacionPersistencia publicacionRepo;

    @Autowired
    private PropiedadPersistencia propiedadRepo;

    // HU 2.4 - Listado

    public List<Publicacion> listarTodas() {
        return publicacionRepo.findByEliminadoFalse();
    }

    public List<Publicacion> listarConFiltros(
            Long propiedadId,
            String ciudad,
            EstadoPublicacion estado,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        return publicacionRepo.findByEliminadoFalse()
                .stream()
                .filter(p -> propiedadId == null
                        || p.getPropiedad().getId().equals(propiedadId))
                .filter(p -> ciudad == null
                        || ciudad.isBlank()
                        || p.getPropiedad().getCiudad()
                                .toLowerCase()
                                .contains(ciudad.toLowerCase()))
                .filter(p -> estado == null
                        || p.getEstado() == estado)
                .filter(p -> precioMin == null
                        || p.getPrecioMensual().compareTo(precioMin) >= 0)
                .filter(p -> precioMax == null
                        || p.getPrecioMensual().compareTo(precioMax) <= 0)
                .collect(Collectors.toList());
    }

    // HU 2.1 - Alta

    public Publicacion crear(Publicacion publicacion) {

        Propiedad propiedad = propiedadRepo
                .findById(publicacion.getPropiedad().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La propiedad no existe."));

        publicacion.setPropiedad(propiedad);

        return publicacionRepo.save(publicacion);
    }

    // HU 2.3 - Modificación

    public Publicacion modificar(Long id, Publicacion datos) {

        Publicacion existente = obtenerOException(id);

        existente.setPrecioMensual(datos.getPrecioMensual());
        existente.setCondiciones(datos.getCondiciones());
        existente.setDescripcion(datos.getDescripcion());
        existente.setFechaPublicacion(datos.getFechaPublicacion());
        existente.setEstado(datos.getEstado());

        return publicacionRepo.save(existente);
    }

    // HU 2.2 - Eliminación lógica

    public void eliminar(Long id) {

        Publicacion publicacion = obtenerOException(id);

        publicacion.setEliminado(true);

        publicacionRepo.save(publicacion);
    }

    public Publicacion obtenerPorId(Long id) {
        return obtenerOException(id);
    }

    public Publicacion obtenerOException(Long id) {

        return publicacionRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la publicación con id " + id));
    }

    public List<Propiedad> obtenerTodasPropiedades() {
        return propiedadRepo.buscarTodasActivas();
    }

    public List<Propiedad> obtenerPropiedadesDisponibles() {
        return propiedadRepo.buscarDisponibles();
    }
}