package tuti.desi.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import tuti.desi.enums.EstadoPublicacion;
import tuti.desi.historial.HistorialEstadoPublicacion;

@Entity
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    private BigDecimal precioMensual;

    @Column(columnDefinition = "TEXT")
    private String condiciones;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDate fechaPublicacion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estado;

    private boolean eliminado;

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL)
    private List<HistorialEstadoPublicacion> historialEstados = new ArrayList<>();

    public Publicacion() {
        this.fechaPublicacion = LocalDate.now();
        this.estado = EstadoPublicacion.ACTIVA;
        this.eliminado = false;
    }

    public void cambiarEstado(EstadoPublicacion nuevoEstado) {

        if (this.estado == nuevoEstado) {
            return;
        }

        HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();

        historial.setEstadoAnterior(this.estado);
        historial.setEstadoNuevo(nuevoEstado);
        historial.setFechaHora(LocalDateTime.now());
        historial.setPublicacion(this);

        historialEstados.add(historial);

        this.estado = nuevoEstado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public List<HistorialEstadoPublicacion> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<HistorialEstadoPublicacion> historialEstados) {
        this.historialEstados = historialEstados;
    }
}