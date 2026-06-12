
package tuti.desi.servicios;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuti.desi.entidades.Contrato;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.persistencia.contratoPersistencia;
 
import java.util.List;
 
@Service
public class contratoServicios {
 
    @Autowired
    private contratoPersistencia contratoRepo;	// Creamos el obj que nos ayude con la persistencia y coneccion con la bd
 
 

    public Contrato crear(Contrato contrato) {
        contrato.cambiarEstado(EstadoContrato.BORRADOR); // registra en historial
        return contratoRepo.save(contrato);
    }
 
 

    public Contrato activar(Long id) {
        Contrato contrato = contratoRepo.findById(id).orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
 
        if (contrato.getEstado() != EstadoContrato.BORRADOR) {
            throw new RuntimeException("Solo se puede activar un contrato en estado BORRADOR");
        }
 
        contrato.cambiarEstado(EstadoContrato.ACTIVO);
        return contratoRepo.save(contrato);
    }
 
 
   
    public Contrato finalizar(Long id) {
        Contrato contrato = contratoRepo.findById(id).orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
 
        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new RuntimeException("Solo se puede finalizar un contrato en estado ACTIVO");
        }
 
        contrato.cambiarEstado(EstadoContrato.FINALIZADO);
        return contratoRepo.save(contrato);
    }
 
 

    public Contrato rescindir(Long id) {
        Contrato contrato = contratoRepo.findById(id).orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
 
        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new RuntimeException("Solo se puede rescindir un contrato en estado ACTIVO");
        }
 
        contrato.cambiarEstado(EstadoContrato.RESCINDIDO);
        return contratoRepo.save(contrato);
    }
 
 

    public Contrato buscarPorId(Long id) {
        return contratoRepo.findById(id).orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
    }
 
 

    public List<Contrato> listarTodos() {
        return contratoRepo.findAll();
    }
 
 

    public List<Contrato> listarPorEstado(EstadoContrato estado) {
        return contratoRepo.findByEstado(estado);
    }
}
 