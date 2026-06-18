package tuti.desi.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.enums.TipoPropiedad;

@Repository
public interface PropiedadPersistencia extends JpaRepository<Propiedad, Long> {

    /**
     * Todas las propiedades no eliminadas.
     */
    @Query("SELECT p FROM Propiedad p WHERE p.eliminada = false")
    List<Propiedad> buscarTodasActivas();

    /**
     * Propiedades disponibles para crear publicaciones.
     */
    @Query("""
            SELECT p
            FROM Propiedad p
            WHERE p.eliminada = false
              AND p.estadoDisp = 'DISPONIBLE'
            """)
    List<Propiedad> buscarDisponibles();

    /**
     * Propiedades de un propietario.
     */
    List<Propiedad> findByPropietarioIdAndEliminadaFalse(Long propietarioId);

    /**
     * Buscar por estado de disponibilidad.
     */
    List<Propiedad> findByEstadoDispAndEliminadaFalse(
            EstadoDisponibilidad estadoDisp);

    /**
     * Buscar por ciudad.
     */
    List<Propiedad> findByCiudadAndEliminadaFalse(String ciudad);

    /**
     * Buscar por tipo de propiedad.
     */
    List<Propiedad> findByTipoAndEliminadaFalse(TipoPropiedad tipo);

    /**
     * Verificar existencia de una propiedad no eliminada.
     */
    boolean existsByIdAndEliminadaFalse(Long id);

}