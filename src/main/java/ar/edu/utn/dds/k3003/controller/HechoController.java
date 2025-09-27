package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.bll.IHechoService;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {

    private final IHechoService hechoService;
    private final MeterRegistry meterRegistry;

    @Autowired
    public HechoController(IHechoService hechoService, MeterRegistry meterRegistry) {
        this.hechoService = hechoService;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<HechoDTO> crear(@RequestBody HechoDTO body) {
        HechoDTO creado = hechoService.addHecho(body);
        this.meterRegistry.counter("Fuentes.hecho.crear").increment();
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HechoDTO> obtener(@PathVariable String id) {
        HechoDTO h = hechoService.getHechoById(id);

        HechoDTO dto = new HechoDTO(
            h.id(),
            h.nombreColeccion(),
            h.titulo(),
            h.etiquetas(),
            h.categoria(),
            h.ubicacion(),
            h.fecha(),
            h.origen()
        );

        this.meterRegistry.counter("Fuentes.hecho.hechoPorId").increment();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/pdi")
    public ResponseEntity<PdIDTO> agregarPDI(@PathVariable String id, @RequestBody PdIDTO body) {
        PdIDTO creado = hechoService.addPdiToHecho(id, body);
        this.meterRegistry.counter("Fuentes.hechos.crearPdi").increment();
        return ResponseEntity.ok(creado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HechoDTO> patch(@PathVariable String id, @RequestBody PatchHechoEstadoRequest body) {
        HechoDTO hecho = hechoService.getHechoById(id);

        if(body.estado().equals("borrado")){
            hechoService.censurarHecho(id);
            this.meterRegistry.counter("Fuentes.hechos.censurados").increment();
        }
        else if(body.estado().equals("activo")){
            hechoService.desCensurarHecho(id);
            this.meterRegistry.counter("Fuentes.hechos.desCensurados").increment();
        }
        else
            return ResponseEntity.badRequest().build();

        this.meterRegistry.counter("Fuentes.hechos.modificarHecho").increment();
        return ResponseEntity.ok(hecho);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        hechoService.borrarHecho(id);
        this.meterRegistry.counter("Fuentes.hechos.borrarHecho").increment();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodos() {
        hechoService.borrarTodosLosHechos();
        this.meterRegistry.counter("Fuentes.hechos.BorrarTodosLosHechos").increment();
        return ResponseEntity.noContent().build();
    }
}