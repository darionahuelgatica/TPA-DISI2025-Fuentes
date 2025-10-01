package ar.edu.utn.dds.k3003.presentacion.controller;

import ar.edu.utn.dds.k3003.business.services.IColeccionService;
import ar.edu.utn.dds.k3003.business.services.IHechoService;
import ar.edu.utn.dds.k3003.presentacion.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.presentacion.dtos.HechoDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionController {

    private final IColeccionService coleccionService;
    private final IHechoService hechoService;
    private final MeterRegistry meterRegistry;

    @Autowired
    public ColeccionController(
            IColeccionService coleccionService,
            IHechoService hechoService,
            MeterRegistry meterRegistry) {
        this.coleccionService = coleccionService;
        this.hechoService = hechoService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    public ResponseEntity<List<ColeccionDTO>> listar() {
        this.meterRegistry.counter("Fuentes.colecciones.listarColecciones").increment();
        return ResponseEntity.ok(coleccionService.getColecciones());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<ColeccionDTO> getByNombre(@PathVariable String nombre) {
        this.meterRegistry.counter("Fuentes.colecciones.mostrarUnaColeccion").increment();
        return ResponseEntity.ok(coleccionService.getColeccionById(nombre));
    }

    @PostMapping
    public ResponseEntity<ColeccionDTO> crear(@RequestBody ColeccionDTO dto) {
        try {
            this.meterRegistry.counter("Fuentes.colecciones.crearColeccion").increment();
            return ResponseEntity.ok(coleccionService.addColeccion(dto));
        } catch (IllegalArgumentException e) {
            this.meterRegistry.counter("Fuentes.colecciones.error").increment();
            return ResponseEntity.status(409).build();
        }
    }

    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> listarPorColeccion(@PathVariable String nombre) {
        List<HechoDTO> lista = hechoService.getHechosByColeccion(nombre);
        this.meterRegistry.counter("Fuentes.colecciones.listarHechosPorColeccion").increment();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{nombre}")
    public ResponseEntity<Void> eliminar(@PathVariable String nombre) {
        coleccionService.deleteColeccion(nombre);
        this.meterRegistry.counter("Fuentes.colecciones.BorrarColeccionPorID").increment();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodas() {
        coleccionService.deleteAllColecciones();
        this.meterRegistry.counter("Fuentes.colecciones.BorrarTodasLasColecciones").increment();
        return ResponseEntity.noContent().build();
    }
}
