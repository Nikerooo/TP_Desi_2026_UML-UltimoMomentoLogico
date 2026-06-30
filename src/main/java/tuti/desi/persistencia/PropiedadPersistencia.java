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

    /**
     * HU 1.1 - Detectar duplicado en alta:
     * ¿existe otra propiedad activa con la misma dirección Y ciudad (sin distinguir mayúsculas)?
     */
    boolean existsByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadaFalse(
            String direccion, String ciudad);

    /**
     * HU 1.3 - Detectar duplicado en edición:
     * igual al anterior pero excluyendo la propiedad que se está editando (por su id).
     */
    boolean existsByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadaFalseAndIdNot(
            String direccion, String ciudad, Long id);

    /**
     * HU 1.4 - Búsqueda combinada por dirección, ciudad, tipo y estado.
     * Si algún parámetro es null o vacío se ignora (actúa como "todos").
     */
    @Query("""
            SELECT p FROM Propiedad p
            WHERE p.eliminada = false
              AND (:direccion IS NULL OR :direccion = '' OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%')))
              AND (:ciudad    IS NULL OR :ciudad    = '' OR LOWER(p.ciudad)    LIKE LOWER(CONCAT('%', :ciudad,    '%')))
              AND (:tipo      IS NULL OR p.tipo      = :tipo)
              AND (:estado    IS NULL OR p.estadoDisp = :estado)
            """)
    List<Propiedad> buscarConFiltros(
            @org.springframework.data.repository.query.Param("direccion") String direccion,
            @org.springframework.data.repository.query.Param("ciudad")    String ciudad,
            @org.springframework.data.repository.query.Param("tipo")      TipoPropiedad tipo,
            @org.springframework.data.repository.query.Param("estado")    EstadoDisponibilidad estado);

}