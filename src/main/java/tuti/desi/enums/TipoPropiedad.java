package tuti.desi.enums;

public enum TipoPropiedad {
    CASA("Casa"), DEPARTAMENTO("Departamento"), LOCAL("Local"), OTRO("Otro");
    private final String descripcion;
    TipoPropiedad(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}
