package tuti.desi.entidades;

import jakarta.persistence.*;
//import jakarta.validation.*;

import tuti.desi.enums.*;




@Entity
public class Propiedad{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// Hace que la PK sea auto incremental
	private long id;
	private String direccion;
	private TipoPropiedad tipo;
	private int cantAmbientes;
	private double mtsCuadrados;
	private String descripcion;
	private String comodidades;
	private EstadoDisponibilidad estadoDisp;
	private boolean eliminada;
	
	
	
	// Constructores
	
	public Propiedad() {
		
	}
	
	public Propiedad(String direccion, TipoPropiedad tipo, int cantAmbientes, double mtsCuadrados, String descripcion,
			String comodidades, EstadoDisponibilidad estadoDisp, boolean eliminada) {
		super();
		this.direccion = direccion;
		this.tipo = tipo;
		this.cantAmbientes = cantAmbientes;
		this.mtsCuadrados = mtsCuadrados;
		this.descripcion = descripcion;
		this.comodidades = comodidades;
		this.estadoDisp = estadoDisp;
		this.eliminada = eliminada;
	}
	
	
	
	
	
	
	//Setter

	public void setId(Long id) {
		this.id = id;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTipo(TipoPropiedad tipo) {
		this.tipo = tipo;
	}

	public void setCantAmbientes(int cantAmbientes) {
		this.cantAmbientes = cantAmbientes;
	}

	public void setMtsCuadrados(double mtsCuadrados) {
		this.mtsCuadrados = mtsCuadrados;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setComodidades(String comodidades) {
		this.comodidades = comodidades;
	}

	public void setEstadoDisp(EstadoDisponibilidad estadoDisp) {
		this.estadoDisp = estadoDisp;
	}

	public void setEliminada(boolean eliminada) {
		this.eliminada = eliminada;
	}
	
	
	
	
	
	
	// Getters

	public Long getId() {
		return id;
	}

	public String getDireccion() {
		return direccion;
	}

	public TipoPropiedad getTipo() {
		return tipo;
	}

	public int getCantAmbientes() {
		return cantAmbientes;
	}

	public double getMtsCuadrados() {
		return mtsCuadrados;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getComodidades() {
		return comodidades;
	}

	public EstadoDisponibilidad getEstadoDisp() {
		return estadoDisp;
	}

	public boolean isEliminada() {
		return eliminada;
	}
	
	
	
	
	
	
	
}