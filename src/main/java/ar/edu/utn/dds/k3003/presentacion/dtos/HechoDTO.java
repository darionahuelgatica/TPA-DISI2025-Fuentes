package ar.edu.utn.dds.k3003.presentacion.dtos;

import ar.edu.utn.dds.k3003.business.enums.CategoriaHechoEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoDTO {
    public HechoDTO(String id, String nombreColeccion, String titulo, List<String> etiquetas,
           CategoriaHechoEnum categoria, String ubicacion, LocalDateTime fecha, String origen) {
        this.id = id;
        this.nombreColeccion = nombreColeccion;
        this.titulo = titulo;
        this.etiquetas = etiquetas;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.origen = origen;
    }

    public HechoDTO(String id,String nombreColeccion, String titulo) {
        this(id, nombreColeccion, titulo, null, null, null, null, null);
    }

    private String id;
    private String nombreColeccion;
    private String titulo;
    private List<String> etiquetas;
    private CategoriaHechoEnum categoria;
    private String ubicacion;
    private LocalDateTime fecha;
    private String origen;
}