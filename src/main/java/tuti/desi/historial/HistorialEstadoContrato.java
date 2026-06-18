package tuti.desi.historial;

import jakarta.persistence.*;
import tuti.desi.entidades.Contrato;
import tuti.desi.enums.EstadoContrato;

import java.time.LocalDateTime;

@Entity
public class HistorialEstadoContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private EstadoContrato estado;

    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    public HistorialEstadoContrato() {}

    public HistorialEstadoContrato(EstadoContrato estado, LocalDateTime fechaHora) {
        this.estado    = estado;
        this.fechaHora = fechaHora;
    }

    public long getId()               { return id; }
    public EstadoContrato getEstado() { return estado; }
    public LocalDateTime getFechaHora(){ return fechaHora; }
    public Contrato getContrato()     { return contrato; }

    public void setId(long id)                  { this.id = id; }
    public void setEstado(EstadoContrato e)      { this.estado = e; }
    public void setFechaHora(LocalDateTime f)    { this.fechaHora = f; }
    public void setContrato(Contrato c)          { this.contrato = c; }
}
