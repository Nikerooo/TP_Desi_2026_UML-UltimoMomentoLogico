package tuti.desi.entidades;

import jakarta.persistence.*;
import tuti.desi.enums.EstadoFactura;
import tuti.desi.enums.MedioPago;
import tuti.desi.historial.HistorialEstadoFactura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private boolean eliminada;
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MedioPago medio;

    private BigDecimal importePago;
    private BigDecimal interes;
    private String conceptoFacturado;

    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoFactura> historialEstados = new ArrayList<>();

    public Factura() {}

    public Long getId()                   { return this.id; }
    public LocalDate getFechaEmision()    { return this.fechaEmision; }
    public LocalDate getFechaVencimiento(){ return this.fechaVencimiento; }
    public BigDecimal getImporte()        { return this.importe; }
    public EstadoFactura getEstado()      { return this.estado; }
    public boolean isEliminada()          { return this.eliminada; }
    public LocalDate getFechaPago()       { return this.fechaPago; }
    public MedioPago getMedio()           { return this.medio; }
    public BigDecimal getImportePago()    { return this.importePago; }
    public BigDecimal getInteres()        { return this.interes; }
    public String getConceptoFacturado()  { return this.conceptoFacturado; }
    public Contrato getContrato()         { return this.contrato; }

    public void setId(Long id)                      { this.id = id; }
    public void setFechaEmision(LocalDate f)        { this.fechaEmision = f; }
    public void setFechaVencimiento(LocalDate f)    { this.fechaVencimiento = f; }
    public void setImporte(BigDecimal i)            { this.importe = i; }
    public void setEstado(EstadoFactura e)          { this.estado = e; }
    public void setEliminada(boolean e)             { this.eliminada = e; }
    public void setFechaPago(LocalDate f)           { this.fechaPago = f; }
    public void setMedio(MedioPago m)               { this.medio = m; }
    public void setImportePago(BigDecimal i)        { this.importePago = i; }
    public void setInteres(BigDecimal i)            { this.interes = i; }
    public void setConceptoFacturado(String c)      { this.conceptoFacturado = c; }
    public void setContrato(Contrato c)             { this.contrato = c; }
}
