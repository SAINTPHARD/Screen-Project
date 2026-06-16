package br.com.pos_graduacao.screenproject.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {

	private Integer temporada;
	private Integer numeroEpisodio;
	private LocalDate dataLancamento; 
	private String titulo;
	private Double avaliacao;         
	
	// Construtor vazio
	public Episodio() {
	}
	
	// Construtor Padrão
	public Episodio(Integer temporada, Integer numeroEpisodio, LocalDate dataLancamento, String titulo, Double avaliacao) {
		this.temporada = temporada;
		this.numeroEpisodio = numeroEpisodio;
		this.dataLancamento = dataLancamento;
		this.titulo = titulo;
		this.avaliacao = avaliacao;
	}

	// Construtor que transforma o DTO (DadosEpisodio) na entidade tratada
	public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
		this.temporada = numeroTemporada;
		this.numeroEpisodio = dadosEpisodio.numeroEpisodio();
		this.titulo = dadosEpisodio.titulo();

		// Trata o "N/A" da API convertendo para Double numérico seguro
		try {
			this.avaliacao = Double.parseDouble(dadosEpisodio.avaliacao());
		} catch (NumberFormatException ex) {
			this.avaliacao = 0.0; 
		}

		// Trata o texto da data convertendo para LocalDate ISO (aaaa-mm-dd)
		try {
			this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
		} catch (DateTimeParseException ex) {
			this.dataLancamento = null; 
		}
	}
	
	// Getters e Setters
	public Integer getTemporada() {
		return temporada;
	}

	public void setTemporada(Integer temporada) {
		this.temporada = temporada;
	}

	public Integer getNumeroEpisodio() {
		return numeroEpisodio;
	}

	public void setNumeroEpisodio(Integer numeroEpisodio) {
		this.numeroEpisodio = numeroEpisodio;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Double getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Double avaliacao) {
		this.avaliacao = avaliacao;
	}

	// ToString limpo e formatado para o console
	@Override
	public String toString() {
		return "Episodio{" +
				"temporada=" + temporada +
				", numeroEpisodio=" + numeroEpisodio +
				", titulo='" + titulo + '\'' +
				", avaliacao=" + avaliacao +
				", dataLancamento=" + dataLancamento +
				'}';
	}
}