package tuti.desi.entidades;

import jakarta.persistence.*;
//import jakarta.validation.*;

import tuti.desi.enums.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity

public class Factura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// Hace que la PK sea auto incremental
	private long id;
	private LocalDate fechaEmision;
	private LocalDate fechaVencimiento;
	private BigDecimal importe;
	private EstadoFactura estado;
	private boolean eliminada;
	private LocalDate fechaPago;
	private MedioPago medio;
	private BigDecimal importePago;
	private BigDecimal interes;
	private String conceptoFacturado;
	
	
	public Factura() {
		
	}
	
	
	public Factura(LocalDate fechaEmision, LocalDate fechaVencimiento, BigDecimal importe, EstadoFactura estado, boolean eliminada, 
			LocalDate fechaPago, MedioPago medio, BigDecimal importePago, BigDecimal interes, String conceptoFacturado) {
		
		this.fechaEmision = fechaEmision;
		this.fechaVencimiento = fechaVencimiento;
		this.importe = importe;
		this.estado= estado;
		this.eliminada=eliminada;
		this.fechaPago = fechaPago;
		this.medio = medio;
		this.importePago = importePago;
		this.interes = interes;
		this.conceptoFacturado = conceptoFacturado;

	}
	
	
    // Getters
	
	public Long getId() {
	    return this.id;
	}
	
	public LocalDate getFechaEmision() {
	    return this.fechaEmision;
	}
	
	public LocalDate getFechaVencimiento() {
	    return this.fechaVencimiento;
	}
	
	public BigDecimal getImporte() {
	    return this.importe;
	}
	
	public boolean isEliminada() {
	    return this.eliminada;
	}
	
	public LocalDate getFechaPago() {
	    return this.fechaPago;
	}
	
	public BigDecimal getImportePago() {
	    return this.importePago;
	}
	
	public BigDecimal getInteres() {
	    return this.interes;
	}
	
	public String getConceptoFacturado() {
	    return this.conceptoFacturado;
	}
	
	public EstadoFactura getEstado() {
		return this.estado;
	}
	
	public MedioPago getMedio() {
		return this.medio;
	}
	    
	
	// ----------- Setters ---------
	
	
    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setImportePago(BigDecimal importePago) {
        this.importePago = importePago;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public void setConceptoFacturado(String conceptoFacturado) {
        this.conceptoFacturado = conceptoFacturado;
    }
    
    public void setMedio(MedioPago medio) {
    	this.medio = medio;
    }
    
    public void setEstado (EstadoFactura estado) {
    	this.estado = estado;
    }
	
	
	
}