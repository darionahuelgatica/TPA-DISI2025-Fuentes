package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.model.PdI;
import ar.edu.utn.dds.k3003.repository.JpaColeccionRepository;
import ar.edu.utn.dds.k3003.repository.JpaHechoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class Fachada implements FachadaFuenteExtended {

    private JpaColeccionRepository colecciones;
    private JpaHechoRepository hechos;
    private FachadaProcesadorPdI procesadorPdI;
    private ObjectMapper mapper;

    @Autowired
    public Fachada(JpaColeccionRepository colecciones, JpaHechoRepository hechos, FachadaProcesadorPdI procesadorPdI) {
        this.colecciones = colecciones;
        this.hechos = hechos;
        this.procesadorPdI = procesadorPdI;
    }

    public Fachada() {
        ConfigurableApplicationContext ctx =
                new SpringApplicationBuilder(ar.edu.utn.dds.k3003.Application.class)
                        .properties(
                                "spring.main.web-application-type=none",
                                "spring.jpa.hibernate.ddl-auto=create-drop",
                                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
                        )
                        .profiles("test")
                        .run();
        this.colecciones = ctx.getBean(JpaColeccionRepository.class);
        this.hechos = ctx.getBean(JpaHechoRepository.class);
    }

    // --------------------
    // COLECCIONES
    // --------------------

    @Override
    public ColeccionDTO agregar(ColeccionDTO dto) {
        boolean yaExiste = colecciones.findById(dto.nombre()).isPresent();
        if (yaExiste) {
            throw new IllegalArgumentException("Ya existe una colección con nombre: " + dto.nombre());
        }

        Coleccion c = new Coleccion();
        c.setNombre(dto.nombre());
        c.setDescripcion(dto.descripcion());
        c.setFechaModificacion(LocalDateTime.now());
        Coleccion guardada = colecciones.save(c);
        return new ColeccionDTO(guardada.getNombre(), guardada.getDescripcion());
    }

    @Override
    public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
        Coleccion col = colecciones.findById(coleccionId)
                .orElseThrow(() -> new NoSuchElementException("No existe la colección: " + coleccionId));
        return new ColeccionDTO(col.getNombre(), col.getDescripcion());
    }

    @Override
    public List<ColeccionDTO> colecciones() {
        return colecciones.findAll().stream()
                .map(c -> new ColeccionDTO(c.getNombre(), c.getDescripcion()))
                .toList();
    }

    //
    public void borrarColeccion(String nombre) {
        Coleccion c = colecciones.findById(nombre)
                .orElseThrow(() -> new NoSuchElementException("No existe la colección: " + nombre));
        colecciones.deleteById(nombre);
    }

    // 🔹
    public void borrarTodasLasColecciones() {
        colecciones.deleteAll();
    }

    @Override
    public void borrarHecho(String id) {
        hechos.deleteById(id);
    }

    @Override
    public void borrarTodosLosHechos() {
        hechos.deleteAll();
    }

    // --------------------
    // HECHOS
    // --------------------

    @Override
    public HechoDTO agregar(HechoDTO hechoDTO) {
        String coleccionId = hechoDTO.nombreColeccion();
        if (colecciones.findById(coleccionId).isEmpty()) {
            throw new IllegalStateException("La colección no existe: " + coleccionId);
        }

        Hecho h = new Hecho(hechoDTO.id(), hechoDTO.nombreColeccion(), hechoDTO.titulo());
        Hecho guardado = hechos.save(h);

        return new HechoDTO(
                guardado.getId(),
                guardado.getColeccionId(),
                guardado.getTitulo(),
                guardado.getEtiquetas(),
                guardado.getCategoria(),
                guardado.getUbicacion(),
                guardado.getFecha(),
                guardado.getOrigen()
        );
    }

    @Override
    public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
        Hecho h = hechos.findById(hechoId).orElseThrow(NoSuchElementException::new);
        return new HechoDTO(
                h.getId(),
                h.getColeccionId(),
                h.getTitulo(),
                h.getEtiquetas(),
                h.getCategoria(),
                h.getUbicacion(),
                h.getFecha(),
                h.getOrigen()
        );
    }

    @Override
    public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
        if (colecciones.findById(coleccionId).isEmpty()) {
            throw new NoSuchElementException("No existe la colección: " + coleccionId);
        }
        List<Hecho> lista = hechos.findByColeccionId(coleccionId);
        return lista.stream()
                .filter(h -> !h.isCensurado() )
                .map(h -> new HechoDTO(
                        h.getId(),
                        h.getColeccionId(),
                        h.getTitulo(),
                        h.getEtiquetas(),
                        h.getCategoria(),
                        h.getUbicacion(),
                        h.getFecha(),
                        h.getOrigen()
                ))
                .collect(Collectors.toList());
    }

    public void censurarHecho(String hechoId) throws NoSuchElementException {
        Hecho h = hechos.findById(hechoId)
                .orElseThrow(() -> new NoSuchElementException("No existe el hecho con ID: " + hechoId));
        h.censurar();
        hechos.save(h);
    }

    public void desCensurarHecho(String hechoId) throws NoSuchElementException {
        Hecho h = hechos.findById(hechoId)
                .orElseThrow(() -> new NoSuchElementException("No existe el hecho con ID: " + hechoId));
        h.setCensurado(false);
        hechos.save(h);
    }

    // --------------------
    // PDI
    // --------------------

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI procesador) {
        this.procesadorPdI = procesador;
    }

    @Override
    public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
        if (this.procesadorPdI == null) {
            throw new IllegalStateException("No se ha configurado el ProcesadorPdI.");
        }

        String hechoId = pdIDTO.hechoId();
        Hecho hecho = hechos.findById(hechoId)
                .orElseThrow(() -> new IllegalStateException("No existe el hecho con ID: " + hechoId));

        if (hecho.isCensurado()) {
            throw new IllegalStateException("El hecho está censurado, no se le puede agregar PdI.");
        }

        PdIDTO resultado = procesadorPdI.procesar(pdIDTO);

        PdI pdiModel = new PdI(
                resultado.id(),
                hechoId,
                resultado.contenido(),
                resultado.etiquetas(),
                true
        );
        hecho.agregarPdI(pdiModel);
        hechos.save(hecho);

        return resultado;
    }
}
