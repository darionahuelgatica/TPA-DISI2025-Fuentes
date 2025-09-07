package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.repository.JpaHechoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {

    private final FachadaFuente fachada;
    private final JpaHechoRepository hechos;

    public HechoController(FachadaFuente fachada, JpaHechoRepository hechos) {
        this.fachada = fachada;
        this.hechos = hechos;
    }

    @PostMapping
    public ResponseEntity<HechoDTO> crear(@RequestBody HechoDTO body) {
        HechoDTO creado = fachada.agregar(body);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HechoDTO> obtener(@PathVariable String id) {
        HechoDTO dto = fachada.buscarHechoXId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/pdi")
    public ResponseEntity<PdIDTO> agregarPDI(@PathVariable String id, @RequestBody PdIDTO body) {
        //body.hechoId=id;
        PdIDTO creado = fachada.agregar(body);
        return ResponseEntity.ok(creado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HechoDTO> patch(@PathVariable String id, @RequestBody PatchHechoEstadoRequest body) {
        Hecho h = hechos.findById(id).orElseThrow();
        if (body.estado() != null && !body.estado().isBlank()) { //por si es "borrado"
            h.setEstado(body.estado());
        }
        Hecho saved = hechos.save(h);
        HechoDTO dto = new HechoDTO(
                saved.getId(),
                saved.getColeccionId(),
                saved.getTitulo(),
                saved.getEtiquetas(),
                saved.getCategoria(),
                saved.getUbicacion(),
                saved.getFecha(),
                saved.getOrigen()
        );
        return ResponseEntity.ok(dto);
    }

}
