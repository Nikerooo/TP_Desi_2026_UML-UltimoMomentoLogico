package tuti.desi.enums;

public enum EstadoFactura {
    PENDIENTE("Pendiente"), PAGADA("Pagada"), VENCIDA("Vencida"), ANULADA("Anulada");
    private final String descripcion;
    EstadoFactura(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}
