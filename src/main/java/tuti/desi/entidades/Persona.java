package tuti.desi.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "Personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_Ciudad")
    public Ciudad ciudad;

    // AGREGADO: eliminado lógico (referenciado en queries del Epic 2)
    @Column(nullable = false)
    private boolean eliminado = false;

    public Persona() {}

    public Persona(String nombre, String apellido, String dni,
                   String telefono, String email, Ciudad ciudad) {
        this.nombre   = nombre;
        this.apellido = apellido;
        this.dni      = dni;
        this.telefono = telefono;
        this.email    = email;
        this.ciudad   = ciudad;
    }

    // Getters
    public Long getId()       { return this.id; }
    public String getNombre() { return this.nombre; }
    // CORREGIDO: typo original "getApellid" → "getApellido"
    public String getApellido() { return this.apellido; }
    public String getDni()    { return this.dni; }
    public String getTelefono(){ return this.telefono; }
    public String getEmail()  { return this.email; }
    public Ciudad getCiudad() { return this.ciudad; }
    public boolean isEliminado() { return eliminado; }

    /** Método auxiliar: devuelve "Apellido, Nombre" para los combos del formulario */
    public String getNombreCompleto() { return apellido + ", " + nombre; }

    // Setters
    public void setId(long id)             { this.id = id; }
    public void setNombre(String nombre)   { this.nombre = nombre; }
    public void setApellido(String ap)     { this.apellido = ap; }
    public void setDni(String dni)         { this.dni = dni; }
    public void setTelefono(String tel)    { this.telefono = tel; }
    public void setEmail(String email)     { this.email = email; }
    public void setCiudad(Ciudad ciudad)   { this.ciudad = ciudad; }
    public void setEliminado(boolean e)    { this.eliminado = e; }
}
