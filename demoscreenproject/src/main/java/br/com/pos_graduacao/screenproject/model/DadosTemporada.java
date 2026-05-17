package br.com.pos_graduacao.screenproject.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Classe de modelo para representar os dados de uma temporada.
 * Responsável por : encapsular os atributos e comportamentos relacionados aos dados de uma temporada, como número da temporada, lista de episódios, etc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporada(
	/**
	 *  O número da temporada é mapeado a partir do campo "Season" no JSON, e a lista de episódios 
	 *  é mapeada a partir do campo "Episodes". A anotação @JsonAlias é usada para garantir que os 
	 *  campos sejam corretamente mapeados mesmo que os nomes no JSON sejam diferentes dos nomes dos atributos na classe
	 */
    @JsonAlias("Season") Integer numero, 
    @JsonAlias("Episodes") List<DadosEpisodio> episodios
) {}