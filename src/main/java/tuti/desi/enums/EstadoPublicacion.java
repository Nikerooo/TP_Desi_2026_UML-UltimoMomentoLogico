// A cargo de: Yamila Esteban - 29100683

package tuti.desi.enums;

// Este enum, representa los posibles estados de una publicación (EPIC 2.1, 2.3)
// Se agregó el campo "descripcion" con getter para que Thymeleaf pueda
// mostrar el texto legible en los templates (por ej: "Activa" en vez de "ACTIVA")


public enum EstadoPublicacion {

    ACTIVA("Activa"),
    PAUSADA("Pausada"),
    FINALIZADA("Finalizada");

    private final String descripcion;

    // Constructor que recibe el texto legible del estado
    
    EstadoPublicacion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter necesario para usar ${est.descripcion} en Thymeleaf
    
    public String getDescripcion() {
        return descripcion;
    }
}
