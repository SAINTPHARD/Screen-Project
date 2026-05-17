package br.com.pos_graduacao.screenproject.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

/**
 * Classe de serviço para consumir APIs externas. 
 * Responsável por : realizar requisições HTTP para obter dados de APIs externas, processar as respostas e lidar com possíveis erros de comunicação.
 * @Service é uma anotação do Spring que indica que a classe é um serviço, ou seja, uma classe que contém lógica de negócios e pode ser injetada em outras partes da aplicação.
 */
@Service
public class ConsumoAPI {

    private final HttpClient client = HttpClient.newHttpClient();

    public String obterDados(String endereco) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
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