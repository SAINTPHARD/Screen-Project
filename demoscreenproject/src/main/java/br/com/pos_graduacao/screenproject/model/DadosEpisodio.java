package br.com.pos_graduacao.screenproject.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe de modelo para representar os dados de um episódio.
 * Responsável por : encapsular os atributos e comportamentos relacionados aos dados de um episódio, como título, número do episódio, temporada, sinopse, etc.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio (
	@JsonAlias("Title") String titulo,				// Título do episódio
	@JsonAlias("Season") String numeroTemporada,	// Número da temporada
	@JsonAlias("Released") String dataLancamento,
	@JsonAlias("Episode") String numeroEpisodio,
	@JsonAlias("imdbRating") String avaliacao
) 
{}
