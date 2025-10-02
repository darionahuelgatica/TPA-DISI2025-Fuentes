package ar.edu.utn.dds.k3003.presentacion.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PdIDTO {

    public PdIDTO(String id, String hechoId, String descripcion, String lugar,
          LocalDateTime momento, String contenido, List<String> etiquetas) {
        this.id = id;
        this.hechoId = hechoId;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.momento = momento;
        this.contenido = contenido;
        this.etiquetas = etiquetas;
    }

    public PdIDTO(String id, String hechoId) {
        this(id, hechoId, null, null, null, null, List.of());
    }

    public PdIDTO(String id) {
        this(id, null, null, null, null, null, List.of());
    }

    private String id;
    private String hechoId;
    private String descripcion;
    private String lugar;
    private LocalDateTime momento;
    private String contenido;
    private List<String> etiquetas;
}