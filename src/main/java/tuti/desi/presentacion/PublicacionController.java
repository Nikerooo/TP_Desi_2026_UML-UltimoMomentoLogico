package tuti.desi.presentacion;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoPublicacion;
import tuti.desi.servicios.PublicacionServicios;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionServicios servicio;

    // HU 2.4 - Listado

    @GetMapping
    public String listado(
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) EstadoPublicacion estado,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Model model) {

        List<Publicacion> publicaciones = servicio.listarConFiltros(
                propiedadId,
                ciudad,
                estado,
                precioMin,
                precioMax);

        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("propiedades", servicio.obtenerTodasPropiedades());
        model.addAttribute("estados", EstadoPublicacion.values());

        model.addAttribute("filtroPropiedadId", propiedadId);
        model.addAttribute("filtroCiudad", ciudad);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroPrecioMin", precioMin);
        model.addAttribute("filtroPrecioMax", precioMax);

        return "publicaciones/listado";
    }

    // HU 2.1 - Alta

    @GetMapping("/nueva")
    public String formularioAlta(Model model) {

        Publicacion publicacion = new Publicacion();
        publicacion.setPropiedad(new Propiedad());

        model.addAttribute("publicacion", publicacion);
        model.addAttribute("propiedades",
                servicio.obtenerPropiedadesDisponibles());
        model.addAttribute("estados",
                EstadoPublicacion.values());
        model.addAttribute("modoEdicion", false);

        return "publicaciones/formulario";
    }

    @PostMapping("/nueva")
    public String procesarAlta(
            @ModelAttribute Publicacion publicacion,
            @RequestParam Long propiedadId,
            RedirectAttributes redirectAttributes) {

        Propiedad propiedad = new Propiedad();
        propiedad.setId(propiedadId);

        publicacion.setPropiedad(propiedad);

        servicio.crear(publicacion);

        redirectAttributes.addFlashAttribute(
                "mensajeExito",
                "Publicación creada correctamente.");

        return "redirect:/publicaciones";
    }

    // HU 2.3 - Modificación

    @GetMapping("/editar/{id}")
    public String formularioEditar(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "publicacion",
                servicio.obtenerPorId(id));

        model.addAttribute(
                "estados",
                EstadoPublicacion.values());

        model.addAttribute(
                "modoEdicion",
                true);

        return "publicaciones/formulario";
    }

    @PostMapping("/editar/{id}")
    public String procesarEdicion(
            @PathVariable Long id,
            @ModelAttribute Publicacion publicacionForm,
            RedirectAttributes redirectAttributes) {

        servicio.modificar(id, publicacionForm);

        redirectAttributes.addFlashAttribute(
                "mensajeExito",
                "Publicación #" + id + " modificada correctamente.");

        return "redirect:/publicaciones";
    }

    // HU 2.2 - Eliminación lógica

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        servicio.eliminar(id);

        redirectAttributes.addFlashAttribute(
                "mensajeExito",
                "Publicación #" + id + " eliminada correctamente.");

        return "redirect:/publicaciones";
    }
}