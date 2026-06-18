package tuti.desi.enums;

public enum EstadoContrato {
    BORRADOR("Borrador"),
    ACTIVO("Activo"),
    FINALIZADO("Finalizado"),
    RESCINDIDO("Rescindido");

    private final String descripcion;
    EstadoContrato(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}
