package ar.edu.utn.dds.k3003.dal.repository;

import ar.edu.utn.dds.k3003.dal.model.Hecho;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HechoRepository {
    Optional<Hecho> findById(String hechoId);
    List<Hecho> findByColeccionId(String coleccionId);
    Hecho save(Hecho h);
    void deleteById(String hechoId);
    void deleteAll();
}