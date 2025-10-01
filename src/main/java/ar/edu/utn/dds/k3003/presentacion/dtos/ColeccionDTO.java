package ar.edu.utn.dds.k3003.presentacion.dtos;

import lombok.Data;

@Data
public class ColeccionDTO {
    private String id;
    private String descripcion;

    public ColeccionDTO(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }
}