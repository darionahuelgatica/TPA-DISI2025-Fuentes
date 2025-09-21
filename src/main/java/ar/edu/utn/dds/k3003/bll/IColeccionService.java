package ar.edu.utn.dds.k3003.bll;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import java.util.List;
import java.util.NoSuchElementException;

public interface IColeccionService {
    ColeccionDTO addColeccion(ColeccionDTO dto);
    ColeccionDTO getColeccionById(String coleccionId) throws NoSuchElementException;
    List<ColeccionDTO> getColecciones();
    void deleteColeccion(String coleccionId) throws NoSuchElementException;
    void deleteAllColecciones();
}
