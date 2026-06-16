package tuti.desi.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Persona;

@Repository	// Indica que esta interfaz forma parte de la capa de persistencia
public interface personaPersistencia extends JpaRepository<Persona, Long> {

	@Query("SELECT p FROM Persona p ")
    List<Persona> buscarTodasPersonas();
	
	
}