package tuti.desi.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuti.desi.entidades.Persona;

import java.util.List;

@Repository
public interface PersonaPersistencia extends JpaRepository<Persona, Long> {

    /** Todas las personas (original) */
    @Query("SELECT p FROM Persona p")
    List<Persona> buscarTodasPersonas();

    /** Personas no eliminadas (para combos de inquilino/propietario) */
    @Query("SELECT p FROM Persona p WHERE p.eliminado = false")
    List<Persona> buscarNoEliminadas();
}
