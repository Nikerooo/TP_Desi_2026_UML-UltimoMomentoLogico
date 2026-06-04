package tuti.desi.entidades;

import tuti.desi.enums.*;
import jakarta.persistence.*;
//import jakarta.validation.*;
import java.time.LocalDateTime;



@Entity
public class Visita{
	
	private long id;
	private LocalDateTime fechaHora;
	private EstadoVisita estado;
	
	
	//Constructor
	
	public Visita() {
		
	}
	
	public Visita(LocalDateTime fechaHora, EstadoVisita estado) {
		super();
		this.fechaHora = fechaHora;
		this.estado = estado;
	}
	
	//
	
}