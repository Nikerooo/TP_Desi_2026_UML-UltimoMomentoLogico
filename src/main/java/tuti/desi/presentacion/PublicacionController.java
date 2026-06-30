// A cargo de: Yamila Esteban - 29100683

package tuti.desi.presentacion;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.enums.EstadoPublicacion;
import tuti.desi.servicios.PublicacionServicios;

// Controller del Epic 2: maneja las peticiones HTTP de las publicaciones
// Mapea todas las rutas bajo /publicaciones

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionServicios servicio;

    // epic 2.1 - Alta: muestra el formulario vacío para cargar una nueva publicación
    // GET /publicaciones/nueva
    @GetMapping("/nueva")
    public String formularioAlta(Model model) {

        // Se crea un objeto vacío para que Thymeleaf pueda enlazarlo con el formulario
        Publicacion publicacion = new Publicacion();
        publicacion.setPropiedad(new Propiedad());

        model.addAttribute("publicacion", publicacion);
        
        // Solo se muestran propiedades DISPONIBLES en el combo de alta
        model.addAttribute("propiedades", servicio.obtenerPropiedadesDisponibles());
        model.addAttribute("estados", EstadoPublicacion.values());
        model.addAttribute("modoEdicion", false);

        return "publicaciones/formulario";
    }

    //EPIC 2.1 - Alta: procesa el formulario enviado
    // POST /publicaciones/nueva
    @PostMapping("/nueva")
    public String procesarAlta(
            @Valid @ModelAttribute Publicacion publicacion,
            BindingResult bindingResult,  // captura errores de validación del @Valid
            @RequestParam Long propiedadId,  // viene separado porque @ModelAttribute no lo bindea
            RedirectAttributes redirectAttributes) {

        // Si hay errores de validación en el formulario, se vuelve al alta con mensaje de error
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensajeError",
                "Completá todos los campos correctamente antes de continuar.");
            return "redirect:/publicaciones/nueva";
        }

        try {
            // Se construye un objeto Propiedad con el ID recibido para que el servicio lo valide
            Propiedad propiedad = new Propiedad();
            propiedad.setId(propiedadId);
            publicacion.setPropiedad(propiedad);

            servicio.crear(publicacion);
            redirectAttributes.addFlashAttribute("mensajeExito", "Publicación creada correctamente.");
        } catch (IllegalArgumentException e) {
        	
            // Si el servicio lanza una excepción por regla de negocio, se muestra el mensaje
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/publicaciones/nueva";
        }

        return "redirect:/publicaciones";
    }
    // EPIC 2.2 - Eliminación lógica
    // POST /publicaciones/eliminar/{id}
    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            servicio.eliminar(id);
            redirectAttributes.addFlashAttribute(
                "mensajeExito", "Publicación #" + id + " eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            // Si la publicación no está en estado ACTIVA, se muestra el error al usuario
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/publicaciones";
    }
    // EPIC 2.3 - Modificación: muestra el formulario con los datos actuales de la publicación
    // GET /publicaciones/editar/{id}
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {

        model.addAttribute("publicacion", servicio.obtenerPorId(id));
        model.addAttribute("estados", EstadoPublicacion.values());
        model.addAttribute("modoEdicion", true);

        return "publicaciones/formulario";
    }

    // epic 2.3 - Modificación: procesa los cambios enviados
    // POST /publicaciones/editar/{id}
    @PostMapping("/editar/{id}")
    public String procesarEdicion(
            @PathVariable Long id,
            @Valid @ModelAttribute Publicacion publicacionForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensajeError",
                "Completá todos los campos correctamente antes de continuar.");
            return "redirect:/publicaciones/editar/" + id;
        }

        try {
            servicio.modificar(id, publicacionForm);
            redirectAttributes.addFlashAttribute(
                "mensajeExito", "Publicación #" + id + " modificada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/publicaciones/editar/" + id;
        }

        return "redirect:/publicaciones";
    }
    // epic 2.4 - Listado con filtros opcionales
    // GET /publicaciones
    @GetMapping
    public String listado(
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) EstadoPublicacion estado,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Model model) {

        // Se aplican los filtros en el servicio y se mandan los resultados a la vista
        List<Publicacion> publicaciones = servicio.listarConFiltros(
                propiedadId, ciudad, estado, precioMin, precioMax);

        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("propiedades", servicio.obtenerTodasPropiedades());
        model.addAttribute("estados", EstadoPublicacion.values());

        // Se guardan los filtros seleccionados para mantenerlos en el formulario
        model.addAttribute("filtroPropiedadId", propiedadId);
        model.addAttribute("filtroCiudad", ciudad);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroPrecioMin", precioMin);
        model.addAttribute("filtroPrecioMax", precioMax);

        return "publicaciones/listado";
    }
   
}
