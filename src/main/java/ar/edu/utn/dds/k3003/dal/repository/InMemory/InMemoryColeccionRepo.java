package ar.edu.utn.dds.k3003.dal.repository.InMemory;

import ar.edu.utn.dds.k3003.dal.model.Coleccion;
import ar.edu.utn.dds.k3003.dal.repository.ColeccionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@Profile("inmemory")
public class InMemoryColeccionRepo implements ColeccionRepository {

    private List<Coleccion> colecciones;

    public InMemoryColeccionRepo() {
        this.colecciones = new ArrayList<>();
    }

    @Override
    public Optional<Coleccion> findById(String id) {
        return this.colecciones.stream().filter(x -> x.getNombre().equals(id)).findFirst();
    }

    @Override
    public Coleccion save(Coleccion col) {
        var coleccionExistente = this.findById(col.getNombre());

        coleccionExistente.ifPresent(coleccion -> this.colecciones.remove(coleccion));

        col.setFechaModificacion(LocalDateTime.now());
        this.colecciones.add(col);

        return col;
    }

    @Override
    public List<Coleccion> findAll() {
        return new ArrayList<>(colecciones);
    }

    @Override
    public void deleteById(String coleccionId) throws NoSuchElementException {
        boolean removed = colecciones.removeIf(col -> col.getNombre().equals(coleccionId));
        if (!removed) {
            throw new NoSuchElementException("Collection not found with id: " + coleccionId);
        }
    }

    @Override
    public void deleteAll() {
        colecciones.clear();
    }
}
