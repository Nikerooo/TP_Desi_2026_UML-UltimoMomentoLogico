package tuti.desi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Persona;

import java.util.ArrayList;

	@Controller
	@RequestMapping("/contratos")
	public class ContratoController {
	
	    @GetMapping("/nuevo")
	    public String formularioNuevo(Model model) {
	        
	
	        Contrato contrato = new Contrato();
	        
	
	        contrato.setPropiedad(new Propiedad());
	        contrato.setInquilino(new Persona());
	        
	
	        model.addAttribute("contrato", contrato);
	        
	
	        model.addAttribute("propiedad.id", new ArrayList<>()); 
	        model.addAttribute("listaInquilinos", new ArrayList<>());   
	        
	        return "cargarContrato";
	    }
	
	    @PostMapping("/nuevo/guardar")
	    public void tomarDatos(@ModelAttribute("contrato") Contrato contrato) {	// 
	    	
	    	System.out.println("Se registro con exito la propiedad con ID: " + contrato.getPropiedad().getId());
	    	System.out.println("Se registro con exito el inquilino con ID: " + contrato.getInquilino().getId());
	    	System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getFechaInicio());
	    	System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getImporteMensual());
	    	System.out.println("Se registro con exito la fecha de inicio: "+ contrato.getDiaVencimientoMensual());
	    	
	    }
	    
	    
	    
	    @GetMapping("/modificar")
	    public String modificarContrato(Model model) {
	    	
	    	return "a";
	    }
	    
	    
	    @PostMapping("/guardar")
	    public String guardarContrato(@ModelAttribute("contrato") Contrato contrato) {
	        
	
	        return "redirect:/contratos/nuevo"; 
	    }
	}