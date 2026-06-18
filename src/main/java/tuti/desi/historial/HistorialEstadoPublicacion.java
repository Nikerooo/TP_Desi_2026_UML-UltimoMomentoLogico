package tuti.desi.historial;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoPublicacion;

@Entity
public class HistorialEstadoPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoNuevo;

    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    public HistorialEstadoPublicacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoPublicacion getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoPublicacion estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoPublicacion getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoPublicacion estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }
}