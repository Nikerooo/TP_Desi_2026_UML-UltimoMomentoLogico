package tuti.desi.entidades;


import jakarta.persistence.*;
//import jakarta.validation.*;

import tuti.desi.enums.*;
import java.math.BigDecimal;
import java.time.LocalDate;



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
	
	
	
	
	
	
}