package br.com.pos_graduacao.screenproject.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class ConsumoAPI {

    private final HttpClient client = HttpClient.newHttpClient();

    // Alterado de 'String s' para 'String endereco'
    public String obterDados(String endereco) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco)) // Agora o Java reconhece a variável
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return response.body();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro de comunicação com a API",
                    e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(
                    "Thread interrompida durante requisição",
                    e
            );
        }
    }
}