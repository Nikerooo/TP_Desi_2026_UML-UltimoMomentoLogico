package tuti.desi.enums;

public enum EstadoPublicacion {
    ACTIVA("Activa"),
    PAUSADA("Pausada"),
    FINALIZADA("Finalizada");

    private final String descripcion;
    EstadoPublicacion(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}
