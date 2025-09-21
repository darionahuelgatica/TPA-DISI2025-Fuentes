package ar.edu.utn.dds.k3003.bll;

import ar.edu.utn.dds.k3003.dal.model.Hecho;
import ar.edu.utn.dds.k3003.dal.model.PdI;
import ar.edu.utn.dds.k3003.dal.repository.ColeccionRepository;
import ar.edu.utn.dds.k3003.dal.repository.HechoRepository;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {

    private final HechoRepository hechoRepository;
    private final ColeccionRepository coleccionRepository;
    private FachadaProcesadorPdI procesadorPdI;

    @Autowired
    public HechoService(
            HechoRepository hechoRepository,
            ColeccionRepository coleccionRepository,
            FachadaProcesadorPdI procesadorPdI) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.procesadorPdI = procesadorPdI;
    }

    @Override
    public HechoDTO addHecho(HechoDTO hechoDTO) {
        String coleccionId = hechoDTO.nombreColeccion();
        if (coleccionRepository.findById(coleccionId).isEmpty())
            throw new IllegalStateException("La colección no existe: " + coleccionId);

        Hecho guardado = hechoRepository.save(
            new Hecho(hechoDTO.id(), hechoDTO.nombreColeccion(), hechoDTO.titulo()));

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
    public HechoDTO getHechoById(String hechoId) throws NoSuchElementException {
        Hecho h = hechoRepository.findById(hechoId)
            .orElseThrow(NoSuchElementException::new);

        if (h.isCensurado())
            throw new NoSuchElementException();

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
    public List<HechoDTO> getHechosByColeccion(String coleccionId) throws NoSuchElementException {
        if (coleccionRepository.findById(coleccionId).isEmpty())
            throw new NoSuchElementException("No existe la colección: " + coleccionId);

        List<Hecho> lista = hechoRepository.findByColeccionId(coleccionId);

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

    @Override
    public void censurarHecho(String hechoId) {
        Hecho h = hechoRepository.findById(hechoId)
            .orElseThrow(() -> new NoSuchElementException("No existe el hecho con ID: " + hechoId));
        h.censurar();
        hechoRepository.save(h);
    }

    @Override
    public void desCensurarHecho(String hechoId) {
        Hecho h = hechoRepository.findById(hechoId)
            .orElseThrow(() -> new NoSuchElementException("No existe el hecho con ID: " + hechoId));
        h.setCensurado(false);
        hechoRepository.save(h);
    }

    @Override
    public PdIDTO addPdiToHecho(String hechoId, PdIDTO pdIDTO) {
        if (this.procesadorPdI == null)
            throw new IllegalStateException("No se ha configurado el ProcesadorPdI.");

        Hecho hecho = hechoRepository.findById(hechoId)
            .orElseThrow(() -> new IllegalStateException("No existe el hecho con ID: " + hechoId));

        if (hecho.isCensurado())
            throw new IllegalStateException("El hecho está censurado, no se le puede agregar PdI.");

        PdIDTO resultado = procesadorPdI.procesar(pdIDTO);

        PdI pdiModel = new PdI(
            resultado.id(),
            hechoId,
            resultado.contenido(),
            resultado.etiquetas(),
            true
        );
        hecho.agregarPdI(pdiModel);
        hechoRepository.save(hecho);

        return resultado;
    }

    @Override
    public void borrarHecho(String id) {
        hechoRepository.deleteById(id);
    }

    @Override
    public void borrarTodosLosHechos() {
        hechoRepository.deleteAll();
    }
}
