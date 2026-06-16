package br.com.pos_graduacao.screenproject.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe de modelo para representar os dados de uma série.
 * Responsável por : encapsular os atributos e comportamentos relacionados aos dados de uma série, como título, ano, gênero, diretor, elenco, etc.
 */

//Ignora os campos que vêm no JSON da API mas que não listamos aqui embaixo
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
 @JsonAlias("Title") String titulo,
 @JsonAlias("Year") String ano,
 @JsonAlias("totalSeasons") Integer totalTemporadas,
 @JsonAlias("imdbRating") String avaliacao,
 @JsonAlias("Genre") String genero,
 @JsonAlias("Director") String diretor,
 @JsonAlias("Actors") String elenco,
 @JsonAlias("Plot") String sinopse,
 @JsonAlias("Poster") String poster
) {
 // Método simples para você concatenar e exibir os dados organizados
 public String obterResumoConcatenado() {
     return "Série: " + titulo + " (" + ano + ")\n" +
            "Temporadas: " + totalTemporadas + " | Nota IMDb: " + avaliacao + "\n" +
            "Gênero: " + genero + " | Diretor: " + diretor + "\n" +
            "Elenco: " + elenco + "\n" +
            "Sinopse: " + sinopse;
 }
}
