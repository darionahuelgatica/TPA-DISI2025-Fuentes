package ar.edu.utn.dds.k3003.dal.repository;

import ar.edu.utn.dds.k3003.dal.model.Coleccion;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ColeccionRepository {
    Coleccion save(Coleccion c);
    Optional<Coleccion> findById(String coleccionId);
    List<Coleccion> findAll();
    void deleteById(String coleccionId) throws NoSuchElementException;
    void deleteAll();
}
