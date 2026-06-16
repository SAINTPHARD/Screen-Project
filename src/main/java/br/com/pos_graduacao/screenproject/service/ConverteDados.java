package br.com.pos_graduacao.screenproject.service;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pos_graduacao.screenproject.model.DadosSerie;

/**
 * Classe de serviço para converter dados.
 * Responsável por : transformar os dados brutos obtidos da API em objetos de modelo (como DadosSerie) que podem ser facilmente manipulados e exibidos na aplicação.
 */

@Component	// Anotação do Spring que indica que esta classe é um componente gerenciado pelo Spring.
public class ConverteDados {
	
	// 1. Criando um objeto ObjectMapper, que é uma classe da biblioteca Jackson usada para converter entre JSON e objetos Java. O ObjectMapper é configurado para ser reutilizado em todo o serviço, evitando a necessidade de criar uma nova instância a cada conversão.
	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Método genérico para converter uma string JSON em um objeto Java do tipo especificado pela classe fornecida como argumento.
	 * @param <T> : tipo genérico que representa o tipo do objeto Java a ser retornado.
	 * @param json : string JSON que contém os dados a serem convertidos.
	 * @param classe: classe do tipo T que representa o tipo do objeto Java a ser criado a partir do JSON. Por exemplo, se você quiser converter o JSON em um objeto do tipo DadosSerie, você passaria DadosSerie.class como argumento.
	 * @return: Retorna um objeto do tipo T, que é o resultado da conversão do JSON para o objeto Java. O tipo T é determinado pela classe fornecida como argumento.
	 */
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Falha ao converter o JSON para o objeto Java", e);
        }
    }
}
