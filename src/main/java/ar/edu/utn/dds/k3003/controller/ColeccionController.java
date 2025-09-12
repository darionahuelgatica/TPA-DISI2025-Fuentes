package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.FachadaFuenteExtended;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.repository.JpaHechoRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionController {

    private final FachadaFuenteExtended fachada;
    private final JpaHechoRepository hechos;
    private final MeterRegistry meterRegistry;

    @Autowired
    public ColeccionController(FachadaFuenteExtended fachada,
                               JpaHechoRepository hechos,
                               MeterRegistry meterRegistry) {
        this.fachada = fachada;
        this.hechos = hechos;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    public ResponseEntity<List<ColeccionDTO>> listar() {
        this.meterRegistry.counter("Fuentes.colecciones.listarColecciones").increment();
        return ResponseEntity.ok(fachada.colecciones());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<ColeccionDTO> porNombre(@PathVariable String nombre) {
        this.meterRegistry.counter("Fuentes.colecciones.mostrarUnaColeccion").increment();
        return ResponseEntity.ok(fachada.buscarColeccionXId(nombre));
    }

    @PostMapping
    public ResponseEntity<ColeccionDTO> crear(@RequestBody ColeccionDTO dto) {
        try {
            this.meterRegistry.counter("Fuentes.colecciones.crearColeccion").increment();
            return ResponseEntity.ok(fachada.agregar(dto));
        } catch (IllegalArgumentException e) {
            this.meterRegistry.counter("Fuentes.colecciones.error").increment();
            return ResponseEntity.status(409).build(); // conflicto si ya existe
        }
    }

    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> listarPorColeccion(@PathVariable String nombre) {
        List<HechoDTO> lista = hechos.findByColeccionId(nombre).stream()
                .map(h -> new HechoDTO(
                        h.getId(), h.getColeccionId(), h.getTitulo(),
                        h.getEtiquetas(), h.getCategoria(), h.getUbicacion(),
                        h.getFecha(), h.getOrigen()
                ))
                .toList();
        this.meterRegistry.counter("Fuentes.colecciones.listarHechosPorColeccion").increment();
        return ResponseEntity.ok(lista);
    }

    // 🔹 DELETE físico de UNA colección
    @DeleteMapping("/{nombre}")
    public ResponseEntity<Void> eliminar(@PathVariable String nombre) {
        ColeccionDTO coleccion = fachada.buscarColeccionXId(nombre);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }
        fachada.borrarColeccion(nombre); // ahora hace borrado físico
        this.meterRegistry.counter("Fuentes.colecciones.BorrarColeccionPorID").increment();
        return ResponseEntity.noContent().build();
    }

    // 🔹 DELETE físico de TODAS las colecciones
    @DeleteMapping
    public ResponseEntity<Void> eliminarTodas() {
        fachada.borrarTodasLasColecciones(); // ahora hace borrado físico
        this.meterRegistry.counter("Fuentes.colecciones.BorrarTodasLasColecciones").increment();
        return ResponseEntity.noContent().build();
    }
}
