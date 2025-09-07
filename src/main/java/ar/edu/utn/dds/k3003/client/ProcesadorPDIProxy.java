package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import java.util.*;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Primary
public class ProcesadorPDIProxy implements FachadaProcesadorPdI {

    private final String endpoint;
    private final ProcesadorPDIRetrofitClient service;

    public ProcesadorPDIProxy(ObjectMapper objectMapper) {

        var env = System.getenv();
        this.endpoint = "https://modulo-procesador-pdi.onrender.com/";

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(ProcesadorPDIRetrofitClient.class);
    }

    @SneakyThrows
    @Override
    public PdIDTO procesar(PdIDTO pdi) throws IllegalStateException {
        Response<PdIDTO> execute = service.procesar(pdi).execute();

        if (execute.isSuccessful()) {
            return execute.body();
        }

        throw new RuntimeException("Error conectandose al componente procesador PDI");
    }

    @Override
    public PdIDTO buscarPdIPorId(String pdiId) throws NoSuchElementException {
        return null;
    }

    @Override
    public List<PdIDTO> buscarPorHecho(String hechoId) throws NoSuchElementException {
        return List.of();
    }

    @Override
    public void setFachadaSolicitudes(FachadaSolicitudes fachadaSolicitudes) {

    }
}