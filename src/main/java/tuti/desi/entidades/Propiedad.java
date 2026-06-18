package tuti.desi.entidades;

import jakarta.persistence.*;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.enums.TipoPropiedad;

@Entity
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;

    // AGREGADO: ciudad como campo String (el enunciado pide dirección + ciudad)
    private String ciudad;

    @Enumerated(EnumType.STRING)
    private TipoPropiedad tipo;

    private int cantAmbientes;
    private double mtsCuadrados;
    private String descripcion;
    private String comodidades;

    @Enumerated(EnumType.STRING)
    private EstadoDisponibilidad estadoDisp;

    // AGREGADO: propietario (requerido por HU 1.1 y listado de contratos)
    @ManyToOne
    @JoinColumn(name = "propietario_id")
    private Persona propietario;

    private boolean eliminada;

    // Constructores
    public Propiedad() {
    }

    public Propiedad(String direccion, String ciudad, TipoPropiedad tipo,
            int cantAmbientes, double mtsCuadrados, String descripcion,
            String comodidades, EstadoDisponibilidad estadoDisp,
            Persona propietario, boolean eliminada) {

        this.direccion = direccion;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.cantAmbientes = cantAmbientes;
        this.mtsCuadrados = mtsCuadrados;
        this.descripcion = descripcion;
        this.comodidades = comodidades;
        this.estadoDisp = estadoDisp;
        this.propietario = propietario;
        this.eliminada = eliminada;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public TipoPropiedad getTipo() {
        return tipo;
    }

    public int getCantAmbientes() {
        return cantAmbientes;
    }

    public double getMtsCuadrados() {
        return mtsCuadrados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getComodidades() {
        return comodidades;
    }

    public EstadoDisponibilidad getEstadoDisp() {
        return estadoDisp;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public boolean isEliminada() {
        return eliminada;
    }

    /**
     * Helper: "Dirección — Ciudad" para combos
     */
    public String getDireccionCompleta() {

        if (ciudad != null) {
            return direccion + " — " + ciudad;
        }

        return direccion;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setTipo(TipoPropiedad tipo) {
        this.tipo = tipo;
    }

    public void setCantAmbientes(int cantAmbientes) {
        this.cantAmbientes = cantAmbientes;
    }

    public void setMtsCuadrados(double mtsCuadrados) {
        this.mtsCuadrados = mtsCuadrados;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setComodidades(String comodidades) {
        this.comodidades = comodidades;
    }

    public void setEstadoDisp(EstadoDisponibilidad estadoDisp) {
        this.estadoDisp = estadoDisp;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }
}