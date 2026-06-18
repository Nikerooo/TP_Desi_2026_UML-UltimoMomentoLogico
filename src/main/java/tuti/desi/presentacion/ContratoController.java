package tuti.desi.presentacion;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.persistencia.contratoPersistencia;
import tuti.desi.persistencia.PersonaPersistencia;
import tuti.desi.persistencia.PropiedadPersistencia;
import tuti.desi.servicios.ContratoServicios;

import java.util.List;

@Controller
@RequestMapping("/contratos")
public class ContratoController {
		
	@Autowired
	private contratoServicios miServicioContrato;
	@Autowired
	private contratoPersistencia contratoRepo;
	@Autowired
	private propiedadPersistencia propiedadRepo; 	
	@Autowired
	private personaPersistencia personaRepo;

	
	
	 @GetMapping("/nuevo")
	 public String formularioNuevo(Model model) {
	        
	    	
		 Contrato contrato = new Contrato();
	     contrato.setPropiedad(new Propiedad());
	     contrato.setInquilino(new Persona());
	        
	     List<Propiedad> propiedadesDisponibles = propiedadRepo.buscarTodasActivas();
	     List<Persona> inquilinosDisponibles = personaRepo.buscarTodasPersonas();
	        
	     model.addAttribute("contrato", contrato);
	     model.addAttribute("listaPropiedades", propiedadesDisponibles); 
	     model.addAttribute("listaInquilinos", inquilinosDisponibles);
	        
	        
	     return "cargarContrato";
	 }
	
	    
	    
	    
	 @PostMapping("/guardar")
	 public String guardarDatos(@Valid @ModelAttribute("contrato") Contrato contrato, BindingResult result, Model model) {
	    	
		 if (result.hasErrors()) {
		        // Si hay errores, recargamos las listas para que los selectores no queden vacíos
		        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
		        model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		        
		        // Devolvemos al usuario a la misma página para que vea los mensajes en rojo
		        return "cargarContrato"; 
		    }

		 
		 try {
			 	System.out.println("Se registro con exito la propiedad con ID: " + contrato.getPropiedad().getId());
				System.out.println("Se registro con exito el inquilino con ID: " + contrato.getInquilino().getId());
				System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getFechaInicio());
			    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getImporteMensual());
			    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getDiaVencimientoMensual());
			    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getDescripcion());
			    	
			    	
			    	
			    miServicioContrato.crear(contrato);
		        miServicioContrato.crear(contrato); // Se llama a la funcion crear
		        return "redirect:/contratos/nuevo";

		    } catch (IllegalArgumentException errorDeNegocio) {

		        model.addAttribute("errorGlobal", errorDeNegocio.getMessage());	// Atrapamos el error y lo mostramos en el HTML
		        
		        // Recargamos las listas para que la página no se rompa
		        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
		        model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		        
		        return "cargarContrato";
		    }


	 }
	    
	    
	    
	 
	 @GetMapping("/modificar")
	 public String prepararContratos(Model model) {
	    	
		Contrato contrato = new Contrato();
		List<Contrato> listaContrato = contratoRepo.buscarTodasActivas();
	    	
	    model.addAttribute("contrato", contrato);
		model.addAttribute("listaContrato", listaContrato); 

	    	
	    return "modificarContrato";
	 }
	    
	 
	 
	 @PostMapping("/prepararModificar")	// Cuando se precione el boton modificar se accede aca y muestra todo para modificar
	 public String prepararModificar(@RequestParam("id") Long id, Model model) {	// se pide el ID para saber que contrato pre-cargar
	     
		 Contrato contratoEncontrado = contratoRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado")); 	
		// Se busca el contrato en base a su ID. Cuando se encuentra se guarda y se trabaja- Caso contrario tira error
		 
		 // Se cargan todos los atributos como persona, propiedad, etc
	     model.addAttribute("contrato", contratoEncontrado);
	     model.addAttribute("listaContratos", contratoRepo.buscarTodasActivas());
	     model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas()); 
	     model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
	     
	     // Se le indica al HTML que el usuario quiere modificar. Para que muestre eso
	     model.addAttribute("modo", "modificar"); 
	     
	     
	     return "modificarContrato";
	 }
	 
	 
	 
	 
	 
	 @PostMapping("/modificar")	// Cuando el usuario quiera guardar los cambios se llega aca
	 public String guardarModificacion(@Valid @ModelAttribute("contrato") Contrato contrato, BindingResult result, Model model) {	// Se 
		    
		 

		    System.out.println("El botón Guardar Cambios fue presionado y llegó al Controlador");

		 
		    if (result.hasErrors()) {	// se validan los errores y se vuelven a cargar la lista de contratos
		    	System.out.println("El formulario falló la validación.");
		    	
		    	result.getFieldErrors().forEach(error -> {
		            System.out.println("-> Error en el campo: '" + error.getField() + "' - Motivo: " + error.getDefaultMessage());
		        });
		    	
		    	
		    	model.addAttribute("listaContrato", contratoRepo.buscarTodasActivas());
		        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas()); 
		        model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		        model.addAttribute("modo", "modificar");
		        return "modificarContrato"; 
		    }

		    try {	// intenta 
		        
		    	System.out.println("✅ 3. Validación exitosa. Viajando hacia la capa de Servicios...");
		        miServicioContrato.modificarContrato(contrato); // Guardar los cambios, si puede
		        
		        
		        return "redirect:/contratos/modificar"; // Volvemos a cargar la pantalla limpia

		    } catch (IllegalArgumentException errorDeNegocio) {
		        
		        model.addAttribute("errorGlobal", errorDeNegocio.getMessage());
		        System.out.println("❌ 4. El servicio rechazó los datos: " + errorDeNegocio.getMessage());
		        
		        // Recargamos todos los combo boxs
		        model.addAttribute("listaContrato", contratoRepo.buscarTodasActivas());
		        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas()); 
		        model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		        model.addAttribute("modo", "modificar");
		        
		        return "modificarContrato";
		    }
	 }	    
}
