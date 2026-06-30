package tuti.desi.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import tuti.desi.enums.EstadoDisponibilidad;
import tuti.desi.enums.TipoPropiedad;
import tuti.desi.historial.HistorialEstadoPropiedad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
    private List<HistorialEstadoPropiedad> historialEstados = new ArrayList<>();

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

    // Registra el nuevo estado en el historial y actualiza el campo de la entidad.
    // Llamar siempre que cambie estadoDisp, tanto en alta como en edición.
    public void cambiarEstado(EstadoDisponibilidad nuevoEstado) {
        this.estadoDisp = nuevoEstado;

        HistorialEstadoPropiedad registro = new HistorialEstadoPropiedad();
        registro.setEstado(nuevoEstado);
        registro.setFechaHora(LocalDateTime.now());
        registro.setPropiedad(this);
        this.historialEstados.add(registro);
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

    public List<HistorialEstadoPropiedad> getHistorialEstados() {
        return historialEstados;
    }

    /**
     * Método auxiliar: devuelve "Dirección — Ciudad" para mostrar en combos
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

    public void setHistorialEstados(List<HistorialEstadoPropiedad> historialEstados) {
        this.historialEstados = historialEstados;
    }
}