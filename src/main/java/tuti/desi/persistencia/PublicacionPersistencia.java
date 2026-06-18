package tuti.desi.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoPublicacion;

@Repository
public interface PublicacionPersistencia
        extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByEliminadoFalse();

    List<Publicacion> findByEstadoAndEliminadoFalse(
            EstadoPublicacion estado);

    boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(
            Long propiedadId,
            EstadoPublicacion estado);

}