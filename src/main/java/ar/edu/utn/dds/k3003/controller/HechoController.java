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
        Hecho h = hechos.findById(id).orElseThrow();

        if ("borrado".equalsIgnoreCase(h.getEstado())) {
            return ResponseEntity.notFound().build(); // no lo mostramos si está borrado
        }

        HechoDTO dto = new HechoDTO(
                h.getId(),
                h.getColeccionId(),
                h.getTitulo(),
                h.getEtiquetas(),
                h.getCategoria(),
                h.getUbicacion(),
                h.getFecha(),
                h.getOrigen()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/pdi")
    public ResponseEntity<PdIDTO> agregarPDI(@PathVariable String id, @RequestBody PdIDTO body) {
        PdIDTO creado = fachada.agregar(body);
        return ResponseEntity.ok(creado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HechoDTO> patch(@PathVariable String id, @RequestBody PatchHechoEstadoRequest body) {
        Hecho h = hechos.findById(id).orElseThrow();
        if (body.estado() != null && !body.estado().isBlank()) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        Hecho h = hechos.findById(id).orElseThrow();
        h.setEstado("borrado"); // borrado lógico
        hechos.save(h);

        return ResponseEntity.noContent().build(); // no devolvemos el registro
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodos() {
        var lista = hechos.findAll();

        lista.forEach(h -> h.setEstado("borrado")); // marcamos todos como borrados
        hechos.saveAll(lista);

        return ResponseEntity.noContent().build(); // no devolvemos nada
    }


}
