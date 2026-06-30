// A cargo de: Yamila Esteban - 29100683

package tuti.desi.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoPublicacion;

// Repositorio de Publicacion - acceso a la base de datos 
// Spring Data JPA genera las consultas automáticamente a partir del nombre del método
@Repository
public interface PublicacionPersistencia extends JpaRepository<Publicacion, Long> {

    // Trae todas las publicaciones que no fueron eliminadas lógicamente (eipc 2.4)
    List<Publicacion> findByEliminadoFalse();

    // Filtra por estado y excluye las eliminadas
    List<Publicacion> findByEstadoAndEliminadoFalse(EstadoPublicacion estado);

    // Verifica si ya existe una publicación activa para una propiedad dada (epic 2.1 y 2.3)
    // Se usa para evitar que una misma propiedad tenga dos publicaciones ACTIVAS al mismo tiempo
    boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(
            Long propiedadId,
            EstadoPublicacion estado);
}
