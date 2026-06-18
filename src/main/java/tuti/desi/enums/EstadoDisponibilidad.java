package tuti.desi.enums;

public enum EstadoDisponibilidad {
    DISPONIBLE("Disponible"),
    RESERVADA("Reservada"),
    ALQUILADA("Alquilada"),
    INACTIVA("Inactiva");

    private final String descripcion;
    EstadoDisponibilidad(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}
