package tuti.desi.entidades;

import jakarta.persistence.*;
import tuti.desi.enums.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;
    private String descripcion;
    @Enumerated(EnumType.STRING)
    private CategoriaIncidente categoria;
    private LocalDateTime fechaAlta;
    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;
    @Enumerated(EnumType.STRING)
    private EstadoIncidente estado;
    private boolean eliminado;
    private LocalDateTime fechaResolucion;
    private String observacionesResolucion;
    private BigDecimal costoResolucion;
    private String responsableTecnico;

    @ManyToOne
    private Contrato contrato;

    public Incidente() {}

    public Long getId()                         { return id; }
    public String getTitulo()                   { return titulo; }
    public String getDescripcion()              { return descripcion; }
    public CategoriaIncidente getCategoria()    { return categoria; }
    public LocalDateTime getFechaAlta()         { return fechaAlta; }
    public Prioridad getPrioridad()             { return prioridad; }
    public EstadoIncidente getEstado()          { return estado; }
    public boolean isEliminado()                { return eliminado; }
    public LocalDateTime getFechaResolucion()   { return fechaResolucion; }
    public String getObservacionesResolucion()  { return observacionesResolucion; }
    public BigDecimal getCostoResolucion()      { return costoResolucion; }
    public String getResponsableTecnico()       { return responsableTecnico; }
    public Contrato getContrato()               { return contrato; }

    public void setId(Long id)                              { this.id = id; }
    public void setTitulo(String t)                         { this.titulo = t; }
    public void setDescripcion(String d)                    { this.descripcion = d; }
    public void setCategoria(CategoriaIncidente c)          { this.categoria = c; }
    public void setFechaAlta(LocalDateTime f)               { this.fechaAlta = f; }
    public void setPrioridad(Prioridad p)                   { this.prioridad = p; }
    public void setEstado(EstadoIncidente e)                { this.estado = e; }
    public void setEliminado(boolean e)                     { this.eliminado = e; }
    public void setFechaResolucion(LocalDateTime f)         { this.fechaResolucion = f; }
    public void setObservacionesResolucion(String o)        { this.observacionesResolucion = o; }
    public void setCostoResolucion(BigDecimal c)            { this.costoResolucion = c; }
    public void setResponsableTecnico(String r)             { this.responsableTecnico = r; }
    public void setContrato(Contrato c)                     { this.contrato = c; }
}
