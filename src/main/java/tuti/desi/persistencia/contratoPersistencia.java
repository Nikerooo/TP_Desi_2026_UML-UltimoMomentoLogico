package tuti.desi.persistencia; 


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;

import java.time.LocalDate;
import java.util.List;

@Repository	// Indica que esta interfaz forma parte de la capa de persistencia
public interface contratoPersistencia extends JpaRepository<Contrato, Long> {

	
	
	List<Contrato> findByEstado(EstadoContrato estado); // Busca contratos por estado (ej: todos los ACTIVOS)
	
	List<Contrato> findByPropiedadId(Long propiedadId); // Busca contratos por id de propiedad

	List<Contrato> findByInquilinoId(Long inquilinoId); // Buscamos contratos por ID de inquilino
	
	
	

	
	
    @Query("SELECT c FROM Contrato c WHERE c.propiedad.id = :propiedadId AND c.estado = 'ACTIVO' AND c.fechaInicio < :fechaFin AND FUNCTION('ADDDATE', c.fechaInicio, c.duracionMeses * 30) > :fechaInicio ")
    List<Contrato> findActivosEnPeriodo(@Param("propiedadId") Long propiedadId, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    // @Param sirve para vincular un parámetro del método Java con un parámetro nombrado dentro de la consulta JPQL	
    

    @Query("SELECT c FROM Contrato c WHERE c.propiedad.id = :propiedadId AND c.estado = :estado")
    List<Contrato> findByPropiedadIdAndEstado(@Param("propiedadId") Long propiedadId, @Param("estado") EstadoContrato estado);
    
	@Query("SELECT c FROM Contrato c")
    List<Contrato> buscarTodasActivas();
}