// A cargo de: Yamila Esteban - 29100683

package tuti.desi.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tuti.desi.enums.EstadoPublicacion;
import tuti.desi.historial.HistorialEstadoPublicacion;

// Entidad principal del Epic 2 - representa una publicación de alquiler 
@Entity
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sin @NotNull acá: en el formulario de edición, la propiedad no se envía por el form,
    // así que Spring la dejaría null y fallaría la validación aunque esté bien cargada.
    // La validación de la propiedad la hace el servicio (crear/modificar).
    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    // Precio con validaciones: requerido y debe ser positivo
    @NotNull(message = "El precio mensual es requerido")
    @Positive(message = "El precio mensual debe ser un número positivo")
    private BigDecimal precioMensual;

    // Condiciones de alquiler: texto largo, no puede estar vacío
    @NotBlank(message = "Las condiciones de alquiler son requeridas")
    @Column(columnDefinition = "TEXT")
    private String condiciones;

    // Descripción de la publicación
    @NotBlank(message = "La descripción es requerida")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "La fecha de publicación es requerida")
    private LocalDate fechaPublicacion;

    @NotNull(message = "El estado es requerido")
    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estado;

    // Eliminación lógica: no se borra de la BD, se marca como eliminado (epic 2.2)
    private boolean eliminado;

    // Relación con el historial de cambios de estado (epic 2.3)
    // cascade=ALL: cuando se guarda la publicación, también se guardan los registros del historial
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL)
    private List<HistorialEstadoPublicacion> historialEstados = new ArrayList<>();

    // Constructor por defecto: inicializa la fecha de hoy y estado ACTIVA
    public Publicacion() {
        this.fechaPublicacion = LocalDate.now();
        this.estado = EstadoPublicacion.ACTIVA;
        this.eliminado = false;
    }

    // Método para cambiar el estado y registrar automáticamente el cambio en el historial
    // Si el estado nuevo es igual al actual, no hace nada (evita registros duplicados)
    public void cambiarEstado(EstadoPublicacion nuevoEstado) {

        if (this.estado == nuevoEstado) {
            return;
        }

        // Se crea el registro histórico con el estado anterior y el nuevo
        HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
        historial.setEstadoAnterior(this.estado);
        historial.setEstadoNuevo(nuevoEstado);
        historial.setFechaHora(LocalDateTime.now());
        historial.setPublicacion(this);

        historialEstados.add(historial);

        // Finalmente se actualiza el estado de la publicación
        this.estado = nuevoEstado;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }

    public BigDecimal getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(BigDecimal precioMensual) { this.precioMensual = precioMensual; }

    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EstadoPublicacion getEstado() { return estado; }
    public void setEstado(EstadoPublicacion estado) { this.estado = estado; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public List<HistorialEstadoPublicacion> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoPublicacion> historialEstados) {
        this.historialEstados = historialEstados;
    }
}
