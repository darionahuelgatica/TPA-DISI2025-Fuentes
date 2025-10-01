package ar.edu.utn.dds.k3003.dataaccess.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.dds.k3003.business.enums.CategoriaHechoEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
@Table(name = "hechos")
public class Hecho {
    @Id
    private String id;

    @Column(name = "coleccion_id", nullable = false)
    private String coleccionId;

    private String titulo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private boolean censurado;

    @Transient
    private final List<PdI> pdis;

    //Agrego viendo clase HechoDTO
    @ElementCollection
    @CollectionTable(name = "hecho_etiquetas", joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "etiqueta")
    private List<String> etiquetas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CategoriaHechoEnum categoria;

    private String ubicacion;

    private String origen;

    public Hecho() {
        this.pdis = new ArrayList<>();
    }
    public Hecho(String id, String coleccionId, String titulo) {
        if (coleccionId == null || coleccionId.isBlank()) {
            throw new IllegalArgumentException("Un Hecho debe tener una colección asociada.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El Hecho debe tener un título.");
        }
        this.id = id;
        this.coleccionId = coleccionId;
        this.titulo = titulo;
        this.fecha = LocalDateTime.now();
        this.censurado = false;
        this.pdis = new ArrayList<>();
    }

    public Hecho(String id, String coleccionId, String titulo, List<PdI> pdis) {
        this.id = id;
        this.coleccionId = coleccionId;
        this.titulo = titulo;
        this.pdis = pdis;
    }

    public void censurar() {
        this.censurado = true;
    }

    public void agregarPdI(PdI pdi) {
        if (pdi == null) {
            throw new IllegalArgumentException("No se puede agregar una PdI nula.");
        }
        this.pdis.add(pdi);
    }

    public String getId() {
        return id;
    }

    public String getColeccionId() {
        return coleccionId;
    }

    public boolean isCensurado() {
        return censurado;
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public List<PdI> getPdis() {
        return pdis;
    }

    public String getOrigen() {
        return origen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public CategoriaHechoEnum getCategoria() {
        return categoria;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setCensurado(boolean censurado) {
        this.censurado = censurado;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public void setCategoria(CategoriaHechoEnum categoria) {
        this.categoria = categoria;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColeccionId(String coleccionId) {
        this.coleccionId = coleccionId;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}