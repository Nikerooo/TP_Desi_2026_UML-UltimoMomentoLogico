package tuti.desi.persistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Propiedad;

@Repository	// Indica que esta interfaz forma parte de la capa de persistencia
public interface propiedadPersistencia extends JpaRepository<Propiedad, Long> {
	
	
	@Query("SELECT p FROM Propiedad p WHERE p.eliminada = false")
    List<Propiedad> buscarTodasActivas();
	
	
	
}