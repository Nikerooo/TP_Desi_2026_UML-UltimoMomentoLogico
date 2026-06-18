package tuti.desi.presentacion;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired private ContratoServicios   miServicioDeContratos;
    @Autowired private contratoPersistencia contratoRepo;
    @Autowired private PropiedadPersistencia propiedadRepo;
    @Autowired private PersonaPersistencia   personaRepo;

    // ── HU 3.1: Formulario de alta ────────────────────────────────────────────

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Contrato contrato = new Contrato();
        contrato.setPropiedad(new Propiedad());
        contrato.setInquilino(new Persona());

        model.addAttribute("contrato", contrato);
        model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
        model.addAttribute("listaInquilinos", personaRepo.buscarNoEliminadas());
        model.addAttribute("modoEdicion", false);
        return "contratos/cargarContrato";
    }

    @PostMapping("/guardar")
    public String guardarDatos(@Valid @ModelAttribute("contrato") Contrato contrato,
                               BindingResult result, Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
            model.addAttribute("listaInquilinos",  personaRepo.buscarNoEliminadas());
            model.addAttribute("modoEdicion", false);
            return "contratos/cargarContrato";
        }

        try {
            miServicioDeContratos.crear(contrato);
            redirectAttributes.addFlashAttribute("mensajeExito", "Contrato creado correctamente.");
            return "redirect:/contratos/listado";
        } catch (Exception e) {
            model.addAttribute("mensajeError", e.getMessage());
            model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
            model.addAttribute("listaInquilinos",  personaRepo.buscarNoEliminadas());
            model.addAttribute("modoEdicion", false);
            return "contratos/cargarContrato";
        }
    }

    // ── HU 3.3: Formulario de edición ─────────────────────────────────────────

    @GetMapping("/modificar/{id}")
    public String formularioModificar(@PathVariable Long id, Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            Contrato contrato = miServicioDeContratos.buscarPorId(id);
            model.addAttribute("contrato", contrato);
            model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
            model.addAttribute("listaInquilinos",  personaRepo.buscarNoEliminadas());
            model.addAttribute("estadosContrato", EstadoContrato.values());
            model.addAttribute("modoEdicion", true);
            return "contratos/cargarContrato";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/contratos/listado";
        }
    }

    @PostMapping("/modificar/{id}")
    public String procesarModificar(@PathVariable Long id,
                                    @Valid @ModelAttribute("contrato") Contrato contrato,
                                    BindingResult result, Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
            model.addAttribute("listaInquilinos",  personaRepo.buscarNoEliminadas());
            model.addAttribute("estadosContrato", EstadoContrato.values());
            model.addAttribute("modoEdicion", true);
            return "contratos/cargarContrato";
        }

        try {
            miServicioDeContratos.modificar(id, contrato);
            redirectAttributes.addFlashAttribute("mensajeExito",
                "Contrato #" + id + " modificado correctamente.");
            return "redirect:/contratos/listado";
        } catch (Exception e) {
            model.addAttribute("mensajeError", e.getMessage());
            model.addAttribute("contrato", miServicioDeContratos.buscarPorId(id));
            model.addAttribute("listaPropiedades", propiedadRepo.buscarTodasActivas());
            model.addAttribute("listaInquilinos",  personaRepo.buscarNoEliminadas());
            model.addAttribute("estadosContrato", EstadoContrato.values());
            model.addAttribute("modoEdicion", true);
            return "contratos/cargarContrato";
        }
    }

    // ── HU 3.2: Eliminación ───────────────────────────────────────────────────

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            miServicioDeContratos.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                "Contrato #" + id + " eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/contratos/listado";
    }

    // ── HU 3.4: Listado ───────────────────────────────────────────────────────

    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) Long propiedadId,
                          @RequestParam(required = false) Long inquilinoId,
                          @RequestParam(required = false) EstadoContrato estado,
                          Model model) {

        List<Contrato> contratos;
        if (propiedadId != null) {
            contratos = estado != null
                ? contratoRepo.findByPropiedadIdAndEstado(propiedadId, estado)
                : contratoRepo.findByPropiedadId(propiedadId);
        } else if (inquilinoId != null) {
            contratos = contratoRepo.findByInquilinoId(inquilinoId);
        } else if (estado != null) {
            contratos = contratoRepo.findByEstado(estado);
        } else {
            contratos = miServicioDeContratos.listarTodos();
        }

        model.addAttribute("contratos", contratos);
        model.addAttribute("propiedades", propiedadRepo.buscarTodasActivas());
        model.addAttribute("inquilinos",  personaRepo.buscarNoEliminadas());
        model.addAttribute("estadosContrato", EstadoContrato.values());
        model.addAttribute("filtroPropiedadId", propiedadId);
        model.addAttribute("filtroInquilinoId", inquilinoId);
        model.addAttribute("filtroEstado", estado);
        return "contratos/listado";
    }

    // Redirect legacy URL
    @GetMapping("/modificar")
    public String redirectListado() { return "redirect:/contratos/listado"; }
}
