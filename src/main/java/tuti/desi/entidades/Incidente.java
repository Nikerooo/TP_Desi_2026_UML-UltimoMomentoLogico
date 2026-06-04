package tuti.desi.entidades;

import jakarta.persistence.*;
//import jakarta.validation.*;

import tuti.desi.enums.*;
import java.math.BigDecimal;
import java.time.*;


@Entity

public class Incidente{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// Hace que la PK sea auto incremental
	private long id;
	private String titulo;
	private String descripcion;
	private CategoriaIncidente categoria;
	private LocalDateTime fechaAlta;
	private Prioridad prioridad;
	private EstadoIncidente estado;
	private boolean eliminado;
	private LocalDateTime fechaResolucion;
	private String observacionesResolucion;
	private BigDecimal costoResolucion;
	private String responsableTecnico;
	
	
	// CONSTRUCTORES
	
	public Incidente() {
		
	}
	
	
	public Incidente(String titulo, String descripcion, CategoriaIncidente categoria, LocalDateTime fechaAlta,
			Prioridad prioridad, EstadoIncidente estado, boolean eliminado, LocalDateTime fechaResolucion,
			String observacionesResolucion, BigDecimal costoResolucion, String responsableTecnico) {
		
		super();
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.fechaAlta = fechaAlta;
		this.prioridad = prioridad;
		this.estado = estado;
		this.eliminado = eliminado;
		this.fechaResolucion = fechaResolucion;
		this.observacionesResolucion = observacionesResolucion;
		this.costoResolucion = costoResolucion;
		this.responsableTecnico = responsableTecnico;
	}

	
	
	
	// GETTERS

	public Long getId() {
		return id;
	}


	public String getTitulo() {
		return titulo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public CategoriaIncidente getCategoria() {
		return categoria;
	}


	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}


	public Prioridad getPrioridad() {
		return prioridad;
	}


	public EstadoIncidente getEstado() {
		return estado;
	}


	public boolean isEliminado() {
		return eliminado;
	}


	public LocalDateTime getFechaResolucion() {
		return fechaResolucion;
	}


	public String getObservacionesResolucion() {
		return observacionesResolucion;
	}


	public BigDecimal getCostoResolucion() {
		return costoResolucion;
	}


	public String getResponsableTecnico() {
		return responsableTecnico;
	}

	
	
	
	
	
	// SETTERS

	public void setId(Long id) {
		this.id = id;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public void setCategoria(CategoriaIncidente categoria) {
		this.categoria = categoria;
	}


	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}


	public void setEstado(EstadoIncidente estado) {
		this.estado = estado;
	}


	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}


	public void setFechaResolucion(LocalDateTime fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}


	public void setObservacionesResolucion(String observacionesResolucion) {
		this.observacionesResolucion = observacionesResolucion;
	}


	public void setCostoResolucion(BigDecimal costoResolucion) {
		this.costoResolucion = costoResolucion;
	}


	public void setResponsableTecnico(String responsableTecnico) {
		this.responsableTecnico = responsableTecnico;
	}
	
	


	
	
	
	
	
	
	
}