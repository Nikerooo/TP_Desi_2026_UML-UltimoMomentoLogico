package tuti.desi.entidades;

import jakarta.persistence.*;
//import jakarta.validation.*;

import tuti.desi.enums.*;
import java.math.BigDecimal;
import java.time.LocalDate;



@Entity
public class Publicacion{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private long id;
	private BigDecimal precioMensual;
	private String condiciones;
	private LocalDate fechaPublicacion;
	private EstadoPublicacion estado;
	private boolean eliminado;
	private String descripcion;
	
	
	// Constructores
	
	public Publicacion() {
		
	}
	
	public Publicacion(BigDecimal precioMensual, String condiciones, LocalDate fechaPublicacion, EstadoPublicacion estado, 
			boolean eliminado, String descripcion) {
		super();
		this.precioMensual = precioMensual;
		this.condiciones = condiciones;
		this.fechaPublicacion = fechaPublicacion;
		this.estado = estado;
		this.eliminado = eliminado;
		this.descripcion = descripcion;
	}

	
	
	
	// Getters
	
	public long getId() {
		return id;
	}

	public BigDecimal getPrecioMensual() {
		return precioMensual;
	}

	public String getCondiciones() {
		return condiciones;
	}

	public LocalDate getFechaPublicacion() {
		return fechaPublicacion;
	}
	
	public EstadoPublicacion getEstado() {
		return estado;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	
	
	
	
	// Setters

	public void setId(long id) {
		this.id = id;
	}

	public void setPrecioMensual(BigDecimal precioMensual) {
		this.precioMensual = precioMensual;
	}

	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}

	public void setFechaPublicacion(LocalDate fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	
	public void setEstado(EstadoPublicacion estado) {
		this.estado = estado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
	
	
}