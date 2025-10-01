package ar.edu.utn.dds.k3003.business.services;

import ar.edu.utn.dds.k3003.presentacion.dtos.ColeccionDTO;
import java.util.List;
import java.util.NoSuchElementException;

public interface IColeccionService {
    ColeccionDTO addColeccion(ColeccionDTO dto);
    ColeccionDTO getColeccionById(String coleccionId) throws NoSuchElementException;
    List<ColeccionDTO> getColecciones();
    void deleteColeccion(String coleccionId) throws NoSuchElementException;
    void deleteAllColecciones();
}
