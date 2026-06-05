package cl.GestionDrones.v1.EmpresasProveedoras.webclient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.GestionDrones.v1.EmpresasProveedoras.dto.BitacoraResponse;
///
@Service
public class BitacorasWebClient {

    @Autowired
    private WebClient webClient;

    private final String URL_BITACORAS =
            "http://localhost:8085/api/v1/bitacoras";

    public List<BitacoraResponse> obtenerTodasLasBitacoras() {

        return webClient
                .get()
                .uri(URL_BITACORAS)
                .retrieve()
                .bodyToFlux(BitacoraResponse.class)
                .collectList()
                .block();
    }

}