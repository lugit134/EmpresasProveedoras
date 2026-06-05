package cl.GestionDrones.v1.EmpresasProveedoras.webclient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.GestionDrones.v1.EmpresasProveedoras.dto.PilotoResponse;

@Service
public class PilotosWebClient {

    @Autowired
    private WebClient webClient;

    private final String URL_PILOTOS =
            "http://localhost:8081/api/v1/pilotos/por-vencer";

    public List<PilotoResponse> obtenerPilotosPorVencer() {

        return webClient
                .get()
                .uri(URL_PILOTOS)
                .retrieve()
                .bodyToFlux(PilotoResponse.class)
                .collectList()
                .block();
    }

}