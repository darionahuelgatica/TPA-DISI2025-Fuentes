package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.FachadaFuenteExtended;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.repository.JpaHechoRepository;
import com.zaxxer.hikari.metrics.IMetricsTracker;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {

    private final FachadaFuenteExtended fachada;
    private final JpaHechoRepository hechos;
    private final MeterRegistry meterRegistry;

    public HechoController(FachadaFuenteExtended fachada, JpaHechoRepository hechos, MeterRegistry meterRegistry) {
        this.fachada = fachada;
        this.hechos = hechos;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<HechoDTO> crear(@RequestBody HechoDTO body) {
        HechoDTO creado = fachada.agregar(body);
        this.meterRegistry.counter("Fuentes.hecho.crear").increment();
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HechoDTO> obtener(@PathVariable String id) {
        Hecho h = hechos.findById(id).orElseThrow();

        if(h.isCensurado()) {
            return ResponseEntity.notFound().build();
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
        this.meterRegistry.counter("Fuentes.hecho.hechoPorId").increment();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/pdi")
    public ResponseEntity<PdIDTO> agregarPDI(@PathVariable String id, @RequestBody PdIDTO body) {
        PdIDTO creado = fachada.agregar(body);
        this.meterRegistry.counter("Fuentes.hechos.crearPdi").increment();
        return ResponseEntity.ok(creado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(@PathVariable String id, @RequestBody PatchHechoEstadoRequest body) {

        Hecho h = hechos.findById(id).orElseThrow();

        if(body.estado().equals("borrado")) {
            this.fachada.censurarHecho(id);
        }
        else if(body.estado().equals("activo")) {
            this.fachada.desCensurarHecho(id);
        }
        else {
            return ResponseEntity.badRequest().build();
        }

        this.meterRegistry.counter("Fuentes.hechos.modificarHecho").increment();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        fachada.borrarHecho(id);
        this.meterRegistry.counter("Fuentes.hechos.borrarHecho").increment();
        return ResponseEntity.noContent().build(); // no devolvemos el registro
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodos() {
        fachada.borrarTodosLosHechos();
        this.meterRegistry.counter("Fuentes.hechos.BorrarTodosLosHechos").increment();
        return ResponseEntity.noContent().build(); // no devolvemos nada
    }


}
