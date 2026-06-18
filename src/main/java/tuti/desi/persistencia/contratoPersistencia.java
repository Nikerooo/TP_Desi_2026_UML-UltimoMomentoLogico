package tuti.desi.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface contratoPersistencia extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEstado(EstadoContrato estado);

    List<Contrato> findByPropiedadId(Long propiedadId);

    List<Contrato> findByInquilinoId(Long inquilinoId);

    @Query("SELECT c FROM Contrato c WHERE c.propiedad.id = :propiedadId AND c.estado = 'ACTIVO' " +
           "AND c.fechaInicio < :fechaFin " +
           "AND FUNCTION('ADDDATE', c.fechaInicio, c.duracionMeses * 30) > :fechaInicio")
    List<Contrato> findActivosEnPeriodo(@Param("propiedadId") Long propiedadId,
                                        @Param("fechaInicio") LocalDate fechaInicio,
                                        @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT c FROM Contrato c WHERE c.propiedad.id = :propiedadId AND c.estado = :estado")
    List<Contrato> findByPropiedadIdAndEstado(@Param("propiedadId") Long propiedadId, @Param("estado") EstadoContrato estado);
    
	@Query("SELECT c FROM Contrato c")
    List<Contrato> buscarTodasActivas();
}
