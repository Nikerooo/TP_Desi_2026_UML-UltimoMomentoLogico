package tuti.desi.entidades;


import jakarta.persistence.*;
//import jakarta.validation.*;

import tuti.desi.enums.*;
import tuti.desi.historial.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;




@Entity
@Table(name="Contrato")

public class Contrato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// Hace que la PK sea auto incremental
	private long id;
	private LocalDate fechaInicio;
	private int duracionMeses;
	private BigDecimal importeMensual;
	private int diaVencimientoMensual;
	private String descripcion;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado") //  Definimos el nombre de la columna
	public EstadoContrato estado;
	
	
	@ManyToOne	// Multiples contratos pueden tener 1 propiedad
	@JoinColumn(name = "propiedad_id", nullable = false)
	private Propiedad propiedad;
	
	
	@ManyToOne		// 1 inquilino puede tener varios contratos?
	@JoinColumn(name = "inquilino_id", nullable = false)
	private Persona inquilino;
	
	
	
	@OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL)	// 1 contrato puede tener muchos incidentes
	private List<Incidente> incidentes = new ArrayList<>();
	
	
	@OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL)	// 1 contrato puede tener varios estados
	private List<HistorialEstadoContrato> historialEstados = new ArrayList<>();
	
	
	@OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL)	// 1 contrato puee tener varias facturas
	private List<Factura> facturas = new ArrayList<>();	
	
	
	
	
	
	
	
	public Contrato() {
		
	}
	
	public Contrato (LocalDate fI, int dM, BigDecimal iM, int dVM, String d, EstadoContrato e) {
		this.fechaInicio = fI;
		this.duracionMeses = dM;
		this.importeMensual = iM;
		this.diaVencimientoMensual = dVM;
		this.descripcion = d;
		this.estado = e;
	}
	
	
	
	
	
	
	
	// Getters
	
	public Long getId() {
		return this.id;
	}
	
	public LocalDate getFechaInicio() {
		return this.fechaInicio;
	}
	
	public int getDuracionMeses() {
		return this.duracionMeses;
	}
	
	public BigDecimal getImporteMensual() {
		return this.importeMensual;
	}
	
	public int getDiaVencimientoMensual() {
		return this.diaVencimientoMensual;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
	
	public EstadoContrato getEstado() {
		return this.estado;
	}
	
	public Propiedad getPropiedad() {
		return propiedad;
	}

	public Persona getInquilino() {
		return inquilino;
	}

	public List<Incidente> getIncidentes() {
		return incidentes;
	}

	public List<HistorialEstadoContrato> getHistorialEstados() {
		return historialEstados;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	
	
	
	
	
	
	// Setters
	

	public void setFechaInicio(LocalDate fechaInico) {
		this.fechaInicio = fechaInico;
	}
	
	public void setDuracionMeses(int duracionMeses) {
		this.duracionMeses = duracionMeses;
	}
	
	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}
	
	public void setDiaVencimientoMensual(int diaVencimientoMensual) {
		this.diaVencimientoMensual = diaVencimientoMensual;
	}
	
	public void setDescripcion (String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}

	public void setIncidentes(List<Incidente> incidentes) {
		this.incidentes = incidentes;
	}

	public void setHistorialEstados(List<HistorialEstadoContrato> historialEstados) {
		this.historialEstados = historialEstados;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	
	
	
	
	public void cambiarEstado(EstadoContrato estado) {
		this.estado = estado;
		
		HistorialEstadoContrato registro = new HistorialEstadoContrato();
		registro.setEstado(estado);
		registro.setFechaHora(LocalDateTime.now());
		registro.setContrato(this);
		
		
		this.historialEstados.add(registro);
		
	}  
	
	
	
	
}