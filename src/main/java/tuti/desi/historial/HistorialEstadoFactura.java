package tuti.desi.historial;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tuti.desi.entidades.Factura;
import tuti.desi.enums.EstadoFactura;

@Entity
@Table(name = "historial_estado_factura")
public class HistorialEstadoFactura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Factura factura;
	private EstadoFactura estado;
	private LocalDateTime fechaCambio;
	
	// CONSTRUCTORES
	public HistorialEstadoFactura() {}
	
	public HistorialEstadoFactura(Long id, Factura factura, EstadoFactura estado, LocalDateTime fechaCambio) {
		super();
		this.id = id;
		this.factura = factura;
		this.estado = estado;
		this.fechaCambio = fechaCambio;
	}

	// GET - SET
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(LocalDateTime fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
	
}
