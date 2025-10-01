package ar.edu.utn.dds.k3003.dataaccess.repository.Jpa;

import ar.edu.utn.dds.k3003.dataaccess.model.Coleccion;
import ar.edu.utn.dds.k3003.dataaccess.repository.ColeccionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface JpaColeccionRepository extends JpaRepository<Coleccion, String>, ColeccionRepository {
}