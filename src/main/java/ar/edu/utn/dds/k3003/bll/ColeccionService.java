package ar.edu.utn.dds.k3003.bll;

import ar.edu.utn.dds.k3003.dal.repository.ColeccionRepository;
import ar.edu.utn.dds.k3003.dal.model.Coleccion;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ColeccionService implements IColeccionService {

    private final ColeccionRepository coleccionRepository;

    @Autowired
    public ColeccionService(ColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }

    @Override
    public ColeccionDTO addColeccion(ColeccionDTO dto) {
        boolean yaExiste = coleccionRepository.findById(dto.nombre()).isPresent();
        if (yaExiste)
            throw new IllegalArgumentException("Ya existe una colección con nombre: " + dto.nombre());

        Coleccion c = new Coleccion();
        c.setNombre(dto.nombre());
        c.setDescripcion(dto.descripcion());
        c.setFechaModificacion(LocalDateTime.now());

        Coleccion guardada = coleccionRepository.save(c);
        return new ColeccionDTO(guardada.getNombre(), guardada.getDescripcion());
    }

    @Override
    public ColeccionDTO getColeccionById(String coleccionId) throws NoSuchElementException {
        Coleccion col = coleccionRepository.findById(coleccionId)
            .orElseThrow(() -> new NoSuchElementException("No existe la colección: " + coleccionId));
        return new ColeccionDTO(col.getNombre(), col.getDescripcion());
    }

    @Override
    public List<ColeccionDTO> getColecciones() {
        return coleccionRepository.findAll().stream()
            .map(c -> new ColeccionDTO(c.getNombre(), c.getDescripcion()))
            .toList();
    }

    @Override
    public void deleteColeccion(String coleccionId) {
        coleccionRepository.findById(coleccionId)
            .orElseThrow(() -> new NoSuchElementException("No existe la colección: " + coleccionId));
        coleccionRepository.deleteById(coleccionId);
    }

    @Override
    public void deleteAllColecciones() {
        coleccionRepository.deleteAll();
    }
}
