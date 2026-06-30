package tuti.desi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.enums.TipoPropiedad;
import tuti.desi.servicios.PropiedadServicio;

import java.util.List;

@Controller
@RequestMapping("/propiedades")
public class PropiedadController {

    @Autowired
    private PropiedadServicio servicio;

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.4 - Listado con filtros
    // GET /propiedades
    // ──────────────────────────────────────────────────────────────────────────

    @GetMapping
    public String listado(
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) TipoPropiedad tipo,
            @RequestParam(required = false) EstadoDisponibilidad estado,
            Model model) {

        List<Propiedad> propiedades = servicio.listarConFiltros(direccion, ciudad, tipo, estado);

        model.addAttribute("propiedades",  propiedades);
        model.addAttribute("tipos",        TipoPropiedad.values());
        model.addAttribute("estados",      EstadoDisponibilidad.values());

        // Conservar los valores de los filtros para que el formulario los muestre al volver.
        model.addAttribute("filtroDireccion", direccion);
        model.addAttribute("filtroCiudad",    ciudad);
        model.addAttribute("filtroTipo",      tipo);
        model.addAttribute("filtroEstado",    estado);

        return "propiedades/listado";
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.1 - Alta de Propiedad
    // GET  /propiedades/nueva  → muestra el formulario vacío
    // POST /propiedades/nueva  → procesa el alta
    // ──────────────────────────────────────────────────────────────────────────

    @GetMapping("/nueva")
    public String formularioAlta(Model model) {
        model.addAttribute("propiedad",   new Propiedad());
        model.addAttribute("personas",    servicio.obtenerPropietariosActivos());
        model.addAttribute("tipos",       TipoPropiedad.values());
        model.addAttribute("estados",     EstadoDisponibilidad.values());
        model.addAttribute("modoEdicion", false);
        return "propiedades/formulario";
    }

    @PostMapping("/nueva")
    public String procesarAlta(
            @ModelAttribute Propiedad propiedad,
            @RequestParam Long propietarioId,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            servicio.crear(propiedad, propietarioId);
            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Propiedad en '" + propiedad.getDireccion() + "' creada correctamente.");
            return "redirect:/propiedades";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorGlobal", e.getMessage());
            model.addAttribute("personas",    servicio.obtenerPropietariosActivos());
            model.addAttribute("tipos",       TipoPropiedad.values());
            model.addAttribute("estados",     EstadoDisponibilidad.values());
            model.addAttribute("modoEdicion", false);
            // Devolvemos el mismo objeto para no perder lo que el usuario escribió.
            model.addAttribute("propiedad", propiedad);
            return "propiedades/formulario";
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.3 - Modificación de Propiedad
    // GET  /propiedades/editar/{id}  → muestra formulario pre-cargado
    // POST /propiedades/editar/{id}  → procesa la modificación
    // ──────────────────────────────────────────────────────────────────────────

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {

        try {
            Propiedad propiedad = servicio.obtenerPorId(id);
            model.addAttribute("propiedad",   propiedad);
            model.addAttribute("personas",    servicio.obtenerPropietariosActivos());
            model.addAttribute("tipos",       TipoPropiedad.values());
            model.addAttribute("estados",     EstadoDisponibilidad.values());
            model.addAttribute("modoEdicion", true);
            return "propiedades/formulario";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorGlobal", e.getMessage());
            return "redirect:/propiedades";
        }
    }

    @PostMapping("/editar/{id}")
    public String procesarEdicion(
            @PathVariable Long id,
            @ModelAttribute Propiedad propiedad,
            @RequestParam Long propietarioId,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            servicio.modificar(id, propiedad, propietarioId);
            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Propiedad #" + id + " modificada correctamente.");
            return "redirect:/propiedades";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorGlobal", e.getMessage());
            model.addAttribute("personas",    servicio.obtenerPropietariosActivos());
            model.addAttribute("tipos",       TipoPropiedad.values());
            model.addAttribute("estados",     EstadoDisponibilidad.values());
            model.addAttribute("modoEdicion", true);
            propiedad.setId(id); // para que el formulario sepa que está en modo edición
            model.addAttribute("propiedad", propiedad);
            return "propiedades/formulario";
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HU 1.2 - Eliminación lógica
    // POST /propiedades/eliminar/{id}
    // ──────────────────────────────────────────────────────────────────────────

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            servicio.eliminar(id);
            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Propiedad #" + id + " eliminada correctamente.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/propiedades";
    }
}
