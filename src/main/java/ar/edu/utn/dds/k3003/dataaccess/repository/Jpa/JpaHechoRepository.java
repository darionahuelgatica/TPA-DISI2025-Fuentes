package ar.edu.utn.dds.k3003.dataaccess.repository.Jpa;

import ar.edu.utn.dds.k3003.dataaccess.model.Hecho;
import ar.edu.utn.dds.k3003.dataaccess.repository.HechoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public interface JpaHechoRepository extends JpaRepository<Hecho, String>, HechoRepository {
    List<Hecho> findByColeccionId(String nombre);
}