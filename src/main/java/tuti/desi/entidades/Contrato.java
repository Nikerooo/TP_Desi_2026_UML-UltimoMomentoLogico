package tuti.desi.entidades;

/**
 * @author NicolasMendez - 44859710
 */


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.historial.HistorialEstadoContrato;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe ingresar una fecha de inicio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @NotNull(message = "Debe ingresar la duración en meses")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracionMeses;

    @NotNull(message = "Debe ingresar el importe mensual")
    @DecimalMin(value = "0.01", message = "El importe debe ser mayor a 0")
    private BigDecimal importeMensual;

    @Min(value = 1, message = "El día de vencimiento debe ser entre 1 y 31")
    @Max(value = 31, message = "El día de vencimiento debe ser entre 1 y 31")
    private int diaVencimientoMensual;

    @NotBlank(message = "Debe ingresar una descripción")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoContrato estado;

    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @ManyToOne
    @JoinColumn(name = "inquilino_id", nullable = false)
    private Persona inquilino;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL)
    private List<Incidente> incidentes = new ArrayList<>();

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL)
    private List<HistorialEstadoContrato> historialEstados = new ArrayList<>();

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL)
    private List<Factura> facturas = new ArrayList<>();

    public Contrato() {}



    public void cambiarEstado(EstadoContrato nuevoEstado) {
        this.estado = nuevoEstado;

        HistorialEstadoContrato registro = new HistorialEstadoContrato();
        registro.setEstado(nuevoEstado);
        registro.setFechaHora(LocalDateTime.now());
        registro.setContrato(this);
        this.historialEstados.add(registro);
    }

    
    // Getters
    
	public Long getId() {
		return id;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public int getDuracionMeses() {
		return duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public int getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public EstadoContrato getEstado() {
		return estado;
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
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

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
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

    
    
}
