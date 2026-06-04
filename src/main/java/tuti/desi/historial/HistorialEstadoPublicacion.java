package tuti.desi.historial;



import tuti.desi.enums.*;
import jakarta.persistence.*;
//import jakarta.validation.*;
import java.time.LocalDateTime;



@Entity
public class HistorialEstadoPublicacion{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private EstadoPublicacion estado;
	private LocalDateTime fechaHora;
	
	
	
	// Constructores
	
	public HistorialEstadoPublicacion() {
		
	}


	public HistorialEstadoPublicacion(EstadoPublicacion estado, LocalDateTime fechaHora) {
		super();
		this.estado = estado;
		this.fechaHora = fechaHora;
	}

	
	
	// Getter
	
	public long getId() {
		return id;
	}


	public EstadoPublicacion getEstado() {
		return estado;
	}


	public LocalDateTime getFechaHora() {
		return fechaHora;
	}
	
	
	
	
	
	// Setters

	public void setId(long id) {
		this.id = id;
	}


	public void setEstado(EstadoPublicacion estado) {
		this.estado = estado;
	}


	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	
	
	
	
}

