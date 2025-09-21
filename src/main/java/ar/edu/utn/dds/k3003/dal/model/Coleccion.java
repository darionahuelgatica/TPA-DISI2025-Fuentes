package ar.edu.utn.dds.k3003.dal.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "colecciones")
public class Coleccion {

  @Id
  @Column(nullable = false, updatable = false)
  private String nombre; // será el ID

  private String descripcion;

  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;

  // ------------------
  // Constructores
  // ------------------
  public Coleccion(String nombre, String descripcion) {
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre de la colección no puede ser vacío.");
    }
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.fechaModificacion = LocalDateTime.now();
  }

  public Coleccion() {
    this.fechaModificacion = LocalDateTime.now();
  }

  // ------------------
  // Getters y Setters
  // ------------------
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public LocalDateTime getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }
}

