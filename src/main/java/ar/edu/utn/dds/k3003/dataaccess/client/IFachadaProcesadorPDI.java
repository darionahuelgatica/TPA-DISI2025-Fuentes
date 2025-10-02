package ar.edu.utn.dds.k3003.dataaccess.client;

import ar.edu.utn.dds.k3003.presentacion.dtos.PdIDTO;
import org.springframework.stereotype.Component;

@Component
public interface IFachadaProcesadorPDI {
    PdIDTO procesar(PdIDTO pdi) throws IllegalStateException;
}