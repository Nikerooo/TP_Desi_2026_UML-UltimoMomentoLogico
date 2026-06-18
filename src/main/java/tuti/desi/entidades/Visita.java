package tuti.desi.entidades;

import jakarta.persistence.*;
import tuti.desi.enums.EstadoVisita;
import java.time.LocalDateTime;

@Entity
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime fechaHora;
    @Enumerated(EnumType.STRING)
    private EstadoVisita estado;

    public Visita() {}
    public Visita(LocalDateTime fechaHora, EstadoVisita estado) {
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public long getId()               { return id; }
    public LocalDateTime getFechaHora(){ return fechaHora; }
    public EstadoVisita getEstado()   { return estado; }
    public void setId(long id)        { this.id = id; }
    public void setFechaHora(LocalDateTime f) { this.fechaHora = f; }
    public void setEstado(EstadoVisita e) { this.estado = e; }
}
