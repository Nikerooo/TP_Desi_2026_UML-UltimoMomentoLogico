// A cargo de: Yamila Esteban - 29100683

package tuti.desi.historial;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoPublicacion;

// Entidad que registra cada cambio de estado de una publicación (epic 2.3)
// Cada vez que se llama a cambiarEstado() en Publicacion, se crea un registro acá
@Entity
public class HistorialEstadoPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Estado desde el que se cambió
    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoAnterior;

    // Estado al que se pasó
    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoNuevo;

    // Fecha y hora exacta del cambio
    private LocalDateTime fechaHora;

    // Relación con la publicación a la que pertenece este registro
    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    public HistorialEstadoPublicacion() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EstadoPublicacion getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoPublicacion estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoPublicacion getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoPublicacion estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Publicacion getPublicacion() { return publicacion; }
    public void setPublicacion(Publicacion publicacion) { this.publicacion = publicacion; }
}
