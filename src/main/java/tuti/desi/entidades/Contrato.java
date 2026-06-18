package tuti.desi.entidades;

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
    private long id;

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

    // ── Método de negocio: cambia estado y registra historial ──────────────
    public void cambiarEstado(EstadoContrato nuevoEstado) {
        this.estado = nuevoEstado;

        HistorialEstadoContrato registro = new HistorialEstadoContrato();
        registro.setEstado(nuevoEstado);
        registro.setFechaHora(LocalDateTime.now());
        registro.setContrato(this);
        this.historialEstados.add(registro);
    }

    // Getters
    public Long getId()                         { return this.id; }
    public LocalDate getFechaInicio()           { return this.fechaInicio; }
    public int getDuracionMeses()               { return this.duracionMeses; }
    public BigDecimal getImporteMensual()       { return this.importeMensual; }
    public int getDiaVencimientoMensual()       { return this.diaVencimientoMensual; }
    public String getDescripcion()              { return this.descripcion; }
    public EstadoContrato getEstado()           { return this.estado; }
    public Propiedad getPropiedad()             { return propiedad; }
    public Persona getInquilino()               { return inquilino; }
    public List<Incidente> getIncidentes()      { return incidentes; }
    public List<HistorialEstadoContrato> getHistorialEstados() { return historialEstados; }
    public List<Factura> getFacturas()          { return facturas; }

    // Setters
    public void setId(long id)                              { this.id = id; }
    public void setFechaInicio(LocalDate f)                 { this.fechaInicio = f; }
    public void setDuracionMeses(int d)                     { this.duracionMeses = d; }
    public void setImporteMensual(BigDecimal i)             { this.importeMensual = i; }
    public void setDiaVencimientoMensual(int d)             { this.diaVencimientoMensual = d; }
    public void setDescripcion(String d)                    { this.descripcion = d; }
    public void setEstado(EstadoContrato e)                 { this.estado = e; }
    public void setPropiedad(Propiedad p)                   { this.propiedad = p; }
    public void setInquilino(Persona i)                     { this.inquilino = i; }
    public void setIncidentes(List<Incidente> i)            { this.incidentes = i; }
    public void setHistorialEstados(List<HistorialEstadoContrato> h) { this.historialEstados = h; }
    public void setFacturas(List<Factura> f)                { this.facturas = f; }
}
