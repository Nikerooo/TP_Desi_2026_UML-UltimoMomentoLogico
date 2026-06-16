package tuti.desi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;


import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Propiedad;
import tuti.desi.persistencia.*;
import tuti.desi.entidades.Persona;
import tuti.desi.servicios.contratoServicios;


import java.util.List;
import java.util.ArrayList;


@Controller
@RequestMapping("/contratos")
public class ContratoController {
		
	@Autowired
	private contratoServicios miServicioDeContratos;
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



		 
		 
		System.out.println("Se registro con exito la propiedad con ID: " + contrato.getPropiedad().getId());
		System.out.println("Se registro con exito el inquilino con ID: " + contrato.getInquilino().getId());
		System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getFechaInicio());
	    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getImporteMensual());
	    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getDiaVencimientoMensual());
	    System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getDescripcion());
	    	
	    	
	    	
	    miServicioDeContratos.crear(contrato);
	    return "redirect:/contratos/nuevo";
	 }
	    
	    
	    
	    @GetMapping("/modificar")
	    public String modificarContrato(Model model) {
	    	
	    	return "a";
	    }
	    
	    
	}