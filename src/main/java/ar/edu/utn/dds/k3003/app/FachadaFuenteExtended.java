package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;

public interface FachadaFuenteExtended extends FachadaFuente {
    void borrarColeccion(String nombre);
    void borrarTodasLasColecciones();
    void borrarHecho (String id);
    void borrarTodosLosHechos();
}
