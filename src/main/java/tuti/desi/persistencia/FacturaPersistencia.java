package tuti.desi.persistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoFactura;

public interface FacturaPersistencia extends JpaRepository<Factura, Long>{
    
	List<Factura> findByEliminada();
		
    // Eliminacion logica
    @Modifying
    @Query("UPDATE Factura f SET f.eliminada = true WHERE f.id = :id AND f.estado != 'PAGADA'")
    int fueEliminada(@Param("id") Long id);
    
	// Filtros
    @Query("SELECT f FROM Factura f WHERE f.eliminada = false " +
            "AND (:contrato   IS NULL OR f.contrato = :contrato) " +
            "AND (:propiedad  IS NULL OR f.contrato.propiedad = :propiedad) " +
            "AND (:inquilino  IS NULL OR f.contrato.inquilino = :inquilino) " +
            "AND (:estado     IS NULL OR f.estado = :estado) " +
            "AND (:fechaDesde IS NULL OR f.fechaVencimiento >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR f.fechaVencimiento <= :fechaHasta)")
     List<Factura> buscar(@Param("contrato")   Contrato contrato,
             						@Param("fechaDesde") LocalDate fechaDesde,
             						@Param("fechaHasta") LocalDate fechaHasta,
                                    @Param("inquilino")  Persona inquilino,
                                    @Param("propiedad")  Propiedad propiedad,
                                    @Param("estado")     EstadoFactura estado);
}
