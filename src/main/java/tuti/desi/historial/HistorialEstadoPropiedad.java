package tuti.desi.historial;

import jakarta.persistence.*;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoDisponibilidad;

import java.time.LocalDateTime;

@Entity
public class HistorialEstadoPropiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoDisponibilidad estado;

    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    public HistorialEstadoPropiedad() {}

    public Long getId()                        { return id; }
    public EstadoDisponibilidad getEstado()    { return estado; }
    public LocalDateTime getFechaHora()        { return fechaHora; }
    public Propiedad getPropiedad()            { return propiedad; }

    public void setId(Long id)                        { this.id = id; }
    public void setEstado(EstadoDisponibilidad e)     { this.estado = e; }
    public void setFechaHora(LocalDateTime f)         { this.fechaHora = f; }
    public void setPropiedad(Propiedad p)             { this.propiedad = p; }
}
