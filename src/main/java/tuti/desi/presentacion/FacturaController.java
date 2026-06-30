package tuti.desi.presentacion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.enums.EstadoFactura;
import tuti.desi.enums.MedioPago;
import tuti.desi.persistencia.ContratoPersistencia;
import tuti.desi.persistencia.FacturaPersistencia;
import tuti.desi.servicios.FacturaServicios;

@Controller 
@RequestMapping("/facturas")
public class FacturaController {
    @Autowired
    private FacturaServicios facturaServicios;
    
    @Autowired
    private FacturaPersistencia facturaRepo;

    @Autowired
    private ContratoPersistencia contratoRepo;
    
    // ALTA
    @GetMapping("/nueva")
    public String facturaNueva(Model model) { // Cajita de envios
        Factura factura = new Factura();
        factura.setContrato(new Contrato());
        model.addAttribute("factura", factura);
        model.addAttribute("listaContratos", contratoRepo.findByEstado(EstadoContrato.ACTIVO));
        model.addAttribute("listaEstados", EstadoFactura.values());
        model.addAttribute("listaMediosPago", MedioPago.values());
        model.addAttribute("modoEdicion", false);

        return "facturas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute("factura") Factura factura, @RequestParam Long contratoId, RedirectAttributes redirectAttributes) {
            Contrato contrato = new Contrato();
            contrato.setId(contratoId);
            factura.setContrato(contrato);
            facturaServicios.crearFactura(factura);

            redirectAttributes.addFlashAttribute("mensajeOk", "Factura creada con exito");
            return "redirect:/facturas";
    }
    
    
    // BAJA (LOGICA)
    @PostMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            facturaServicios.eliminarFactura(id);
            redirectAttributes.addFlashAttribute("mensajeOk", id + "factura eliminada correctamente.");

        return "redirect:/facturas";
    }
    
    
    // MODIFICACION
    @GetMapping("/editar/{id}")
    public String editarFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaServicios.obtenerFacturaPorId(id);

        model.addAttribute("factura", factura);
        model.addAttribute("listaContratos", contratoRepo.findByEstado(EstadoContrato.ACTIVO));
        model.addAttribute("listaEstados", EstadoFactura.values());
        model.addAttribute("listaMediosPago", MedioPago.values());
        model.addAttribute("modoEdicion", true);

        return "facturas/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarFactura(@PathVariable Long id, @ModelAttribute("factura") Factura factura, RedirectAttributes redirectAttributes) {
            facturaServicios.actualizarFactura(id, factura);
            redirectAttributes.addFlashAttribute("mensajeExito", id + "factura modificada");

            return "redirect:/facturas";
    }
    
    
    // LISTADO
    @GetMapping
    public String listarFacturas(
    		@RequestParam(required = false) Long contratoId,
    		@RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = false) Long inquilinoId,    		
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) EstadoFactura estado,
            Model model) {

        // Si hay datos los pintamos sino pintamos null
        Contrato contrato = contratoId != null ? new Contrato() : null;
        if (contrato != null) contrato.setId(contratoId);

        Persona inquilino = inquilinoId != null ? new Persona() : null;
        if (inquilino != null) inquilino.setId(inquilinoId);
        
        Propiedad propiedad = propiedadId != null ? new Propiedad() : null;
        if (propiedad != null) propiedad.setId(propiedadId);

        List<Factura> facturas = facturaServicios.listarFacturas(contrato, fechaDesde, fechaHasta, inquilino, propiedad, estado);

        // Cargamos los datos
        model.addAttribute("facturas", facturas);
        model.addAttribute("listaContratos", contratoRepo.findByEstado(EstadoContrato.ACTIVO));
        model.addAttribute("listaEstados", EstadoFactura.values());
        model.addAttribute("filtroContratoId", contratoId);
        model.addAttribute("filtroFechaDesde", fechaDesde);
        model.addAttribute("filtroFechaHasta", fechaHasta);
        model.addAttribute("filtroInquilinoId", inquilinoId);
        model.addAttribute("filtroPropiedadId", propiedadId);
        model.addAttribute("filtroEstado", estado);


        return "facturas/listado";
    }
}
