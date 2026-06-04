package tuti.desi.entidades;

import jakarta.persistence.*;
//import jakarta.validation.*;



@Entity		// Indica que esta es una clase
@Table(name = "Personas")		// Indica que hay que crear la tabla con nombre Personas
public class Persona {
	
	@Id	// Indica que el atributo ID es la Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// Hace que la PK sea auto incremental
	private Long id;
	private String nombre;
	private String apellido;
	private String dni;
	private String telefono;
	private String email;
	
	@ManyToOne
	@JoinColumn(name="id_Ciudad")
	public Ciudad ciudad;
	
	
	// Constructores
	
	public Persona () {
		
	}
	
	
	public Persona (String nombre, String apellido, String dni, String telefono, String email, Ciudad ciudad) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.telefono = telefono;
		this.email = email;
		this.ciudad = ciudad;
	}
	
	
	
	// Getters
	
	public Long getId() {
		return this.id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getApellid() {
		return this.apellido;
	}
	
	public String getDni() {
		return this.dni;
	}
	
	public String getTelefono() {
		return this.telefono;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public Ciudad getCiudad() {
		return this.ciudad;
	}
	
	
	
	// setters
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public void setTelefono (String telefono) {
		this.telefono = telefono;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	
	
	
	
	
	
	
	
}
