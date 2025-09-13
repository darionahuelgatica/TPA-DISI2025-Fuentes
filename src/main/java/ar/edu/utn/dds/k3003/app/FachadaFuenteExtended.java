package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;

public interface FachadaFuenteExtended extends FachadaFuente {
    void censurarHecho(String hechoId);
    void desCensurarHecho(String hechoId);
    void borrarColeccion(String nombre);
    void borrarTodasLasColecciones();
    void borrarHecho (String id);
    void borrarTodosLosHechos();
}
