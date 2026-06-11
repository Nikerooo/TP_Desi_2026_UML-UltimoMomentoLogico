package tuti.desi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contratos")
public class ContratoController {

    @GetMapping("/nuevo")
    public String formularioNuevo() {
        return "cargarContrato";
    }
}