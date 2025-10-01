package ar.edu.utn.dds.k3003.dataaccess.client;

import ar.edu.utn.dds.k3003.presentacion.dtos.PdIDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProcesadorPDIRetrofitClient {
    @POST("api/PdIs")
    Call<PdIDTO> procesar(@Body PdIDTO pdIDTO);
}