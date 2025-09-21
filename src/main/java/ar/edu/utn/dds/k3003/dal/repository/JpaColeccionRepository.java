package ar.edu.utn.dds.k3003.dal.repository;

import ar.edu.utn.dds.k3003.dal.model.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaColeccionRepository extends JpaRepository<Coleccion, String>{
} 