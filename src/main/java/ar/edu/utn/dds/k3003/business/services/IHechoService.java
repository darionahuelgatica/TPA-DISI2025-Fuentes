package ar.edu.utn.dds.k3003.business.services;

import ar.edu.utn.dds.k3003.presentacion.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.presentacion.dtos.PdIDTO;

import java.util.List;
import java.util.NoSuchElementException;

public interface IHechoService {
    HechoDTO addHecho(HechoDTO hecho);
    HechoDTO getHechoById(String hechoId) throws NoSuchElementException;
    List<HechoDTO> getHechosByColeccion(String coleccionId) throws NoSuchElementException;
    void censurarHecho(String hechoId);
    void desCensurarHecho(String hechoId);
    PdIDTO addPdiToHecho(String hechoId, PdIDTO pdIDTO);
    void borrarHecho (String id);
    void borrarTodosLosHechos();
}