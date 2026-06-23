package tuti.desi.presentacion;

/**
 * @author NicolasMendez - 44859710
 */

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;import org.springframework.validation.BindingResult;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.persistencia.*;
import tuti.desi.persistencia.PersonaPersistencia;
import tuti.desi.persistencia.PropiedadPersistencia;
import tuti.desi.servicios.ContratoServicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Controller	// Esto le indica al HTML esta clase es el controller 
@RequestMapping("/contratos")	// Mappea las peticiones webs hacia /contratos
public class ContratoController {
		
	@Autowired	// Spring inyecta las dependencias requeridas para poder llamar a la clase
	private ContratoServicios miServicioContrato;	// Declaramos una variable que nos sirve para llamar a las funciones de la capa de servicios
	@Autowired
	private ContratoPersistencia contratoRepo;	// Al igual que las variables de la capa de persistencia
	@Autowired
	private PropiedadPersistencia propiedadRepo; 	
	@Autowired
	private PersonaPersistencia personaRepo;

	
	
	 @GetMapping("/nuevo")	// Entrega los datos necesarios para que se carguen los comboBoxs, etc
	 public String formularioNuevo(Model model) {	// El Obj Model es el encargado de mandar o llevar todos los datos requeridos. Es como una cajita de envios
	        
	    	
		 Contrato contrato = new Contrato();	// Se crean las variables que necesitamos mandar al sitio web
	     contrato.setPropiedad(new Propiedad());	// Se precargan para que no devuelva error
	     contrato.setInquilino(new Persona());
	        
	     List<Propiedad> propiedadesDisponibles = propiedadRepo.buscarTodasActivas();	// Se llenan las listas con las propiedades e inquilinos
	     List<Persona> inquilinosDisponibles = personaRepo.buscarTodasPersonas();
	        
	     model.addAttribute("contrato", contrato);	// Se le asigna a contrato el obj contrato
	     model.addAttribute("listaPropiedades", propiedadesDisponibles); 	// Se le asigna a listaPropiedades el obj List propiedadesDisponibles. Eso va a 
	     model.addAttribute("listaInquilinos", inquilinosDisponibles);
	        
	        
	     return "cargarContrato";	// Retorna 
	 }
	
	    
	    
	    
	 @PostMapping("/guardar")	// recibe los datos enviados por el formulario HTML.
	 public String guardarDatos(@Valid @ModelAttribute("contrato") Contrato contrato, BindingResult result, Model model) {
		 // @ModelAttribute: Toma los textos que el usuario escribió y los transforma automáticamente en un objeto Java
		 //@Valid: Revisa que los datos ingresados sean correctos según las reglas de tu clase.
		 // BindingResult: Si el @Valid encuentra datos incorrectos o vacíos, anota los errores aquí.
		 
		 
		 if (result.hasErrors()) {
		        // Si hay errores, recargamos las listas para que los selectores no queden vacíos
		        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
		        model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		        
		        // Devolvemos al usuario a la misma página para que vea los mensajes en rojo
		        return "cargarContrato"; 
		    }

		 
		 try {		// Va a intentar guardar los cambios, en caso de que no pueda, tira error
			 
			 
			 	// Mensajes "buchones" para saber si se estan guardando bien los datos
			 	System.out.println("Se registro con exito la propiedad con ID: " + contrato.getPropiedad().getId());
				System.out.println("Se registro con exito el inquilino con ID: " + contrato.getInquilino().getId());
				System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getFechaInicio());
			    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getImporteMensual());
			    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getDiaVencimientoMensual());
			    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getDescripcion());
			    	
			    	
			    	

		        miServicioContrato.crear(contrato); // Se llama a la funcion crear en la capa de servicios para plasmarlo en la BD
		        return "redirect:/contratos/nuevo"; // Retorna a la URL

		    } catch (IllegalArgumentException errorDeNegocio) {	// Si no puede, se va a mostrar el error

		        model.addAttribute("errorGlobal", errorDeNegocio.getMessage());	// Atrapamos el error y lo mostramos en el HTML
		        
		        // Recargamos las listas para que la página no se rompa
		        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
		        model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		        
		        return "cargarContrato";
		    }


	 }
	    
	    
	    
	 @GetMapping("/modificar")	// Cuando se quiera modificar 
	 public String prepararContratos(@RequestParam(required = false) Long idPropiedad, @RequestParam(required = false) Long idInquilino, @RequestParam(required = false) EstadoContrato estado,@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio, Model model) {
		    // RequestParam agarra los datos que se especifica
		    
		    model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());  // Rellenamos los comboBoxs
		    model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		    
		    
		    List<Contrato> listaFiltrada = contratoRepo.findByEstadoNot(EstadoContrato.BORRADO); // Traemos los contratos menos los borrados
		    
	    	
	    	
		    if (idPropiedad != null) {	// Validamos que el filtro no este vacio para hacer la busqueda. En caso de que si lo este, no modifica en nada
		    	List<Contrato> listaTemporal = new ArrayList<>();	// Creamos una lista temporal
		    	

		    	for(Contrato c : listaFiltrada) {	// Recorremos la lista sin los borrados
		    		
		    		if(c.getPropiedad().getId().equals(idPropiedad)) {	// Nos fijamos cuales propiedades coinciden con el ID de la propiedad requerida
		    			listaTemporal.add(c);	// Lo añadimos a la temporal
		    		}
		    		
		    	}
		    	listaFiltrada = listaTemporal;	// Pegamos la lista temporal en la lista Filtrada
		    }
		    
		    
		    	// Mismo proceso
		    if (idInquilino != null) {
		    	List<Contrato> listaTemporal = new ArrayList<>();
		    	
		    	for(Contrato c : listaFiltrada) {
		    		
		    		if(c.getInquilino().getId().equals(idInquilino)) {
		    			listaTemporal.add(c);
		    		}
		    		
		    	}
		    	listaFiltrada = listaTemporal;
		    }
		    
		    
		    
		    if (estado != null) {
		    	List<Contrato> listaTemporal = new ArrayList<>();
		    	
		    	for(Contrato c : listaFiltrada) {
		    		if(c.getEstado() == estado) {
		    			listaTemporal.add(c);
		    		}
		    	}
		    	listaFiltrada=listaTemporal;
		    }
		    
		    
		 // Filtro para la Fecha de Inicio
		    if (fechaInicio != null) {
		        List<Contrato> listaTemporal = new ArrayList<>();
		        
		        for (Contrato c : listaFiltrada) {
		            // Validamos que el contrato tenga fecha y que coincida exactamente con la buscada
		            if (c.getFechaInicio() != null && c.getFechaInicio().equals(fechaInicio)) {
		                listaTemporal.add(c);
		            }
		        }
		        
		        listaFiltrada = listaTemporal;
		    }
		    
		    
		    model.addAttribute("listaContrato", listaFiltrada);	// Cargamos la lista filtrada y completa
		    
 
		    model.addAttribute("contrato", new Contrato()); // Enviamos un contrato vacío para evitar errores de null en los botones de radio o formularios
		    
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
		        
		    	System.out.println("Validación exitosa. Viajando hacia la capa de Servicios...");
		        miServicioContrato.modificarContrato(contrato); // Guardar los cambios, si puede
		        
		        
		        return "redirect:/contratos/modificar"; // Volvemos a cargar la pantalla limpia

		    } catch (IllegalArgumentException errorDeNegocio) {
		        
		        model.addAttribute("errorGlobal", errorDeNegocio.getMessage());
		        System.out.println("El servicio rechazó los datos: " + errorDeNegocio.getMessage());
		        
		        // Recargamos todos los combo boxs
		        model.addAttribute("listaContrato", contratoRepo.buscarTodasActivas());
		        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas()); 
		        model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
		        model.addAttribute("modo", "modificar");
		        
		        return "modificarContrato";
		    }

	 }
	 
	 
	 @PostMapping("/prepararBorrar")	
	 public String prepararBorrar(@RequestParam("id") Long id, Model model) {	
	      
	     // Buscamos el contrato para poder mostrar sus datos en la pantalla de confirmación
	     Contrato contratoEncontrado = contratoRepo.findById(id)
	             .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado")); 	
	     
	     model.addAttribute("contrato", contratoEncontrado);
	     model.addAttribute("listaContrato", contratoRepo.findByEstadoNot(EstadoContrato.BORRADO)); 
	     

	     model.addAttribute("modo", "borrar"); 
	     
	     return "modificarContrato";
	 }
	 
	 
	 @PostMapping("/Borrar")	
	 public String Borrar(@RequestParam("id") Long id, Model model, Contrato contrato) {	
	     
		 try {	
			 
	            System.out.println("Ejecutando borrado lógico del contrato ID: " + id);
	            
	            miServicioContrato.borradoLogicoContrato(id); 
	            
	            return "redirect:/contratos/modificar"; 

	        } catch (IllegalArgumentException errorDeNegocio) {
	            
	            model.addAttribute("errorGlobal", errorDeNegocio.getMessage());
	            
	            model.addAttribute("contrato", new Contrato());
	            
	            model.addAttribute("listaContrato", contratoRepo.findByEstadoNot(EstadoContrato.BORRADO));
	            model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas()); 
	            model.addAttribute("listaInquilinos", personaRepo.buscarTodasPersonas());
	            
	            return "modificarContrato";
	        }
	     
	 }
	 
	 
	 
	 
	 
	 

		    
}

