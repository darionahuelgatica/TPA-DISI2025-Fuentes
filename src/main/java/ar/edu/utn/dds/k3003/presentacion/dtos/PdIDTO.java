package ar.edu.utn.dds.k3003.presentacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdIDTO {

    public PdIDTO(String id, String hechoId) {
        this(id, hechoId, null, null, null, null, null, List.of());
    }

    public PdIDTO(String id, String hechoId, String url) {
        this(id, hechoId, url, null, null, null, null, List.of());
    }

    public PdIDTO(String id) {
        this(id, null, null,null, null, null, null, List.of());
    }

    private String id;
    private String hechoId;
    private String url;
    private String descripcion;
    private String lugar;
    private LocalDateTime momento;
    private String contenido;
    private List<String> etiquetas;
}