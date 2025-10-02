package ar.edu.utn.dds.k3003.dataaccess.client;

import ar.edu.utn.dds.k3003.presentacion.dtos.PdIDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Primary
@Component
public class ProcesadorPDIProxy implements IFachadaProcesadorPDI {

    private ProcesadorPDIRetrofitClient retrofitClient;
    private final ObjectMapper objectMapper;

    @Value("${procesadorPdi.url}")
    private String url;

    @Autowired
    public ProcesadorPDIProxy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IllegalArgumentException {

        if(url == null || url.isBlank())
            throw new IllegalArgumentException("No se configuró la url de Fuentes");

        var retrofit =
            new Retrofit.Builder()
                .baseUrl(this.url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.retrofitClient = retrofit.create(ProcesadorPDIRetrofitClient.class);
    }

    @SneakyThrows
    @Override
    public PdIDTO procesar(PdIDTO pdi) throws IllegalStateException {
        Response<PdIDTO> execute = retrofitClient.procesar(pdi).execute();

        if (execute.isSuccessful())
            return execute.body();

        throw new RuntimeException("Error conectandose al componente procesador PDI");
    }
}