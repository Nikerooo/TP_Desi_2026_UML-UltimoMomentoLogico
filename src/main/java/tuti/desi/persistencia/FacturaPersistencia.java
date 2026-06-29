package tuti.desi.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuti.desi.entidades.Factura;

public interface FacturaPersistencia extends JpaRepository<Factura, Long>{
    
	List<Factura> findByEliminada();
	
	// Faltan filtros (TO - DO) 
    
    // Eliminacion logica
    @Modifying
    @Query("UPDATE Factura f SET f.eliminada = true WHERE f.id = :id AND f.estado != 'PAGADA'")
    int fueEliminada(@Param("id") Long id);
}
