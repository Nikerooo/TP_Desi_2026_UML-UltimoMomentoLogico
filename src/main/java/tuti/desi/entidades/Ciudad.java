package tuti.desi.entidades;

import jakarta.persistence.*;
//import jakarta.validation.*;

@Entity
@Table (name="Ciudad")
public class Ciudad {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nombre;
	
	@ManyToOne()
	private Provincia provincia;
	
	public Ciudad() {
		
	}
	
	public Ciudad (String nombre, Provincia provincia) {
		this.nombre = nombre;
		this.provincia = provincia;
	}
	
	
	// Getters
	
	public Long getId() {
		return this.id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public Provincia getProvincia() {
		return this.provincia;
	}
	
	// Setters
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void serProvincia (Provincia provincia) {
		this.provincia = provincia;
	}
	
}
