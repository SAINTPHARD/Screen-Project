package br.com.pos_graduacao.screenproject.principal;

import br.com.pos_graduacao.screenproject.model.DadosSerie;
import br.com.pos_graduacao.screenproject.service.ConsumoAPI;
import br.com.pos_graduacao.screenproject.service.ConverteDados;
import java.util.Scanner;

public class Principal {

	private final Scanner leitura = new Scanner(System.in);
	private final ConsumoAPI consumo = new ConsumoAPI();
	private final ConverteDados conversor = new ConverteDados();

	private static final String ENDERECO = "https://www.omdbapi.com/?t=";
	private static final String API_KEY = "&apikey=6585022c";

	public void exibirMenu() {
		System.out.println("Bem-vindo ao Sistema de Filmes/Séries!");

		System.out.println("Digite o nome da série para a busca:");
		var nomeSerie = leitura.nextLine();

		// Busca o JSON
		var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

		// Converte para o Record
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

		// MODIFICAÇÃO AQUI: Chama o resumo formatado que você criou
		System.out.println("\n-------------------------------------------");
		System.out.println(dados.obterResumoConcatenado());
		System.out.println("-------------------------------------------");

		System.out.println("\n4. Sair");
	}
}