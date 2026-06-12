package tuti.desi.historial;



import tuti.desi.enums.*;
import jakarta.persistence.*;
//import jakarta.validation.*;
import java.time.LocalDateTime;
import tuti.desi.entidades.Contrato;



@Entity
public class HistorialEstadoContrato{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private EstadoContrato estado;
	private LocalDateTime fechaHora;
	
	
	
	@ManyToOne
	@JoinColumn(name = "contrato_id")
	private Contrato contrato;
	
	
	// Constructores
	
	public HistorialEstadoContrato() {
		
	}


	public HistorialEstadoContrato(EstadoContrato estado, LocalDateTime fechaHora) {
		super();
		this.estado = estado;
		this.fechaHora = fechaHora;
	}

	
	
	// Getter
	
	public long getId() {
		return id;
	}


	public EstadoContrato getEstado() {
		return estado;
	}


	public LocalDateTime getFechaHora() {
		return fechaHora;
	}
	
	public Contrato getContrato() {
		return this.contrato;
	}
	
	
	
	
	
	// Setters

	public void setId(long id) {
		this.id = id;
	}


	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}


	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	
	
	public void añadirHistorial() {
		
	}
	
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	
	
	
}