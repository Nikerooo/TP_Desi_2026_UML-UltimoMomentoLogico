package tuti.desi.entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Provincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "provincia")
    private List<Ciudad> ciudades;

    public Long getId()            { return id; }
    public void setId(Long id)     { this.id = id; }
    public String getNombre()      { return nombre; }
    public void setNombre(String n){ this.nombre = n; }
    public List<Ciudad> getCiudades() { return ciudades; }
}
