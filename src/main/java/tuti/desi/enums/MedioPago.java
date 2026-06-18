package tuti.desi.enums;

public enum MedioPago {
    TRANSFERENCIA("Transferencia"), EFECTIVO("Efectivo"), DEBITO("Débito"), CREDITO("Crédito");
    private final String descripcion;
    MedioPago(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}
