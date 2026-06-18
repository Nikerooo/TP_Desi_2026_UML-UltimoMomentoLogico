
package tuti.desi.servicios;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entidades.Contrato;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.persistencia.*;
import tuti.desi.entidades.*; 

import java.util.List;
 
@Service
public class contratoServicios {
 
    @Autowired
    private contratoPersistencia contratoRepo;	// Creamos el obj que nos ayude con la persistencia y coneccion con la bd
    @Autowired
    private personaPersistencia personaRepo;
    @Autowired
    private propiedadPersistencia propiedadRepo;

    
    
    
    public Contrato crear(Contrato contrato) {
    	
    	
    	Long idPropiedad = contrato.getPropiedad().getId();
        Long idInquilino = contrato.getInquilino().getId();


        Propiedad propiedadExistente = propiedadRepo.findById(idPropiedad).orElse(null);
        Persona inquilinoExistente = personaRepo.findById(idInquilino).orElse(null);


        if (propiedadExistente == null || inquilinoExistente == null) {
            throw new IllegalArgumentException("La propiedad o el inquilino no existen en la BD.");
        }
    	
        
        if (propiedadExistente.isEliminada() != false) { 
            throw new IllegalArgumentException("La propiedad seleccionada no se encuentra disponible actualmente.");
        }


        List<Contrato> contratosActivos = contratoRepo.findByPropiedadIdAndEstado(idPropiedad, EstadoContrato.ACTIVO);
        if (!contratosActivos.isEmpty()) {
            throw new IllegalArgumentException("Esta propiedad ya posee un contrato ACTIVO vigente.");
        }
        
        
        contrato.cambiarEstado(EstadoContrato.BORRADOR); // registra en historial
        propiedadExistente.setEstadoDisp(EstadoDisponibilidad.ALQUILADA);	// Cambiamos el estado de la propiedad

        return contratoRepo.save(contrato);
    }
 

    @Transactional 	// Hace que la operacion sea atomica, todo o nada
    public Contrato modificarContrato (Contrato contratoMod) {
    	
    	Contrato contratoOriginal = contratoRepo.findById(contratoMod.getId())	// Buscamos el contrato que coincida con el ID del contrato que el usuario quiso modificar
                .orElseThrow(() -> new IllegalArgumentException("El contrato no existe."));	// Si no se encuentra lanza un mensaje de error. 
    	

    	Long idPropiedadOriginal = contratoOriginal.getPropiedad().getId();	// Obtenemos el ID de la propiedad asignada al contrato original
        Long idPropiedadNueva = contratoMod.getPropiedad().getId();	// Obtenemos el ID de la propiedad al que el usuario quiere modificar
        
        Long idInquilinoOriginal = contratoOriginal.getInquilino().getId();	// Mismo procedimiento con el inquilino
        Long idInquilinoNuevo = contratoMod.getInquilino().getId();
        

        EstadoContrato estadoAnterior = contratoOriginal.getEstado();	// Buscamos el estado original del estado
        EstadoContrato estadoNuevo = contratoMod.getEstado();	// Buscamos el nuevo estado que quiere establecer el usuario
        Propiedad propiedadActual = contratoOriginal.getPropiedad();	// Buscamos a que propiedad esta ligado el "viejo" contrato
    	
        System.out.print("Se accede perfectamente a la capa de servicios");	// TEST
        
       
        
        if (!idPropiedadOriginal.equals(idPropiedadNueva)) {	// Si id de la propiedad seleccionada por el usuario no coincide con el ya establecido accede
            
            Propiedad propiedadNueva = propiedadRepo.findById(idPropiedadNueva).orElseThrow(() -> new IllegalArgumentException("La nueva propiedad no existe."));
            //Buscamos y guardamos la nueva propiedad en base a su ID
            
            
            if (contratoOriginal.getEstado() == EstadoContrato.ACTIVO) { //Si el contrato ya está activo, hay que hacer enroque de estados
                
                
                if (propiedadNueva.isEliminada() || "ALQUILADA".equals(propiedadNueva.getEstadoDisp())) { // Validamos que la nueva propiedad cumpla los criterios de aceptación
                    throw new IllegalArgumentException("No se puede transferir el contrato: la nueva propiedad no está disponible.");
                }
                
                
                List<Contrato> activosEnNueva = contratoRepo.findByPropiedadIdAndEstado(idPropiedadNueva, EstadoContrato.ACTIVO); // Buscamos si las propiedades tienen un contrato activo
                if (!activosEnNueva.isEmpty()) {	// Si la lista no esta vacia es que esa propiedad ya tiene un contracto activo
                    throw new IllegalArgumentException("La nueva propiedad elegida ya posee un contrato ACTIVO.");
                }

                
                
                Propiedad propiedadVieja = contratoOriginal.getPropiedad(); 
                propiedadVieja.setEstadoDisp(EstadoDisponibilidad.DISPONIBLE); // Liberamos la propiedad vieja
                propiedadRepo.save(propiedadVieja);

                
                propiedadNueva.setEstadoDisp(EstadoDisponibilidad.ALQUILADA); // Ocupamos la propiedad nueva
                propiedadRepo.save(propiedadNueva);
            }

            
            contratoOriginal.setPropiedad(propiedadNueva); // Vinculamos la nueva propiedad al contrato
        }

        


        if (!idInquilinoOriginal.equals(idInquilinoNuevo)) {	// Si el 
            Persona inquilinoNuevo = personaRepo.findById(idInquilinoNuevo)
                    .orElseThrow(() -> new IllegalArgumentException("El nuevo inquilino no existe."));
            
            contratoOriginal.setInquilino(inquilinoNuevo);
        }




        if (estadoAnterior != estadoNuevo) {
            
            
            if (estadoAnterior == EstadoContrato.FINALIZADO || estadoAnterior == EstadoContrato.RESCINDIDO) {
                throw new IllegalArgumentException("Operación denegada: No se puede modificar ni reactivar un contrato que ya se encuentra Finalizado o Rescindido.");
            }	// Si el estado del contrato original es finalizado o rescindido, no se le puede cambiar el estado

            
            if (estadoAnterior == EstadoContrato.BORRADOR && estadoNuevo != EstadoContrato.ACTIVO) {
                throw new IllegalArgumentException("Transición inválida: Un contrato en estado BORRADOR solo puede cambiar a ACTIVO.");
            }	// Si el estado anterior es borrador y el nuevo es distinto a activo, no se puede cambiar a otro que no sea activo

            
            if (estadoAnterior == EstadoContrato.ACTIVO && (estadoNuevo != EstadoContrato.FINALIZADO && estadoNuevo != EstadoContrato.RESCINDIDO)) {
                throw new IllegalArgumentException("Transición inválida: Un contrato ACTIVO solo puede cambiar a FINALIZADO o RESCINDIDO.");
            }	// Si el estado es activo, no se puede cambiar a otro que no sea finalizado o rescindido

            
            
            
            if (estadoNuevo == EstadoContrato.ACTIVO) {
                
                if (propiedadActual.isEliminada() || EstadoDisponibilidad.ALQUILADA.equals(propiedadActual.getEstadoDisp())) {	// Si la propiedadesta eliminada o alquilada tira error por que no esta disponible
                    throw new IllegalArgumentException("No se puede activar el contrato: La propiedad asignada no está disponible.");
                }
                propiedadActual.setEstadoDisp(EstadoDisponibilidad.ALQUILADA); // Si no, se le asigna el nuevo estado de Alquilada
            } 
            
            
            
            
            
            if (estadoNuevo == EstadoContrato.FINALIZADO || estadoNuevo == EstadoContrato.RESCINDIDO) {
            
                propiedadActual.setEstadoDisp(EstadoDisponibilidad.DISPONIBLE);
            }

            propiedadRepo.save(propiedadActual);
            contratoOriginal.cambiarEstado(estadoNuevo);
        }
        
        
        
        contratoOriginal.setDuracionMeses(contratoMod.getDuracionMeses());
        contratoOriginal.setImporteMensual(contratoMod.getImporteMensual());
        contratoOriginal.setDiaVencimientoMensual(contratoMod.getDiaVencimientoMensual());
        contratoOriginal.setDescripcion(contratoMod.getDescripcion());



        return contratoRepo.save(contratoOriginal);
        
    }
    
    
    
    
    
    
    
    

    public Contrato buscarContratoPorId(Long id) {
        return contratoRepo.findById(id).orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
    }
 
 

    public List<Contrato> listarTodos() {
        return contratoRepo.findAll();
    }
 
 

    public List<Contrato> listarPorEstado(EstadoContrato estado) {
        return contratoRepo.findByEstado(estado);
    }
    
    
    
}
 