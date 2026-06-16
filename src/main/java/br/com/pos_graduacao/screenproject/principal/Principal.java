package br.com.pos_graduacao.screenproject.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.pos_graduacao.screenproject.model.DadosSerie;
import br.com.pos_graduacao.screenproject.model.DadosTemporada;
import br.com.pos_graduacao.screenproject.model.Episodio;
import br.com.pos_graduacao.screenproject.service.ConsumoAPI;
import br.com.pos_graduacao.screenproject.service.ConverteDados;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=6585022c";

    public void exibirMenu() {
        System.out.println("Digite o nome da série para busca:");
        var nomeSerie = leitura.nextLine();

        // =========================================================================
        // ETAPA 1: BUSCA DE DADOS NA API
        // =========================================================================

        // 1. Busca e exibe os dados gerais da série (mapeamento cru do Jackson)
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println("\n--- DADOS DA SÉRIE ---");
        System.out.println(dados);

        // 2. Loop tradicional para buscar os dados de todas as temporadas disponíveis
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        // =========================================================================
        // ETAPA 2: TRABALHANDO COM COLEÇÕES, LAMBDAS E METHOD REFERENCES
        // =========================================================================

        // 3. Exibe a lista completa de temporadas usando Method Reference (::)
        System.out.println("\n--- LISTA DE TEMPORADAS (COMPLETA) ---");
        temporadas.forEach(System.out::println);

        // 4. Lambdas aninhadas (->): Percorre cada temporada e imprime apenas o título de cada episódio
        System.out.println("\n--- TÍTULOS DOS EPISÓDIOS (LAMBDA TRADICIONAL) ---");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        // =========================================================================
        // ETAPA 3: EVOLUÇÃO PARA OPERAÇÕES ENCADEADAS COM STREAMS
        // =========================================================================

        // 5. O Poder do Stream: Filtrando, ordenando e exibindo o Top 5 episódios da API
        System.out.println("\n--- TOP 5 EPISÓDIOS COM MAIOR AVALIAÇÃO ---");
        temporadas.stream() // 1. ABRE A ESTEIRA (Source)
                .flatMap(t -> t.episodios().stream()) // 2. INTERMEDIÁRIA: Une todas as listas de episódios em um único fluxo contínuo
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")) // 3. INTERMEDIÁRIA: Ignora episódios que não possuem nota cadastrada
                .sorted((e1, e2) -> Double.compare(Double.parseDouble(e2.avaliacao()), Double.parseDouble(e1.avaliacao()))) // 4. INTERMEDIÁRIA: Ordena de forma decrescente (do maior para o menor)
                .limit(5) // 5. INTERMEDIÁRIA: Limita o fluxo para reter apenas os 5 primeiros
                .forEach(e -> System.out.println(e.titulo() + " - Nota: " + e.avaliacao())); // 6. TERMINAL: Executa as operações e imprime o resultado

        /*// =========================================================================
        // EXTRA: TESTE CONCEITUAL ISOLADO COM STREAMS
        // =========================================================================
        System.out.println("\n--- TESTE ISOLADO COM LISTA DE NOMES ---");
        List<String> nomes = Arrays.asList("João", "Maria", "Pedro", "Robedson", "Gesse");

        nomes.stream()
                .sorted()               // Ordena em ordem alfabética: ["Gesse", "João", "Maria", "Pedro", "Robedson"]
                .limit(3)               // Filtra os 3 primeiros: ["Gesse", "João", "Maria"]
                .forEach(System.out::println); // Imprime linha por linha no terminal*/
        
        // =========================================================================
        // Lista de episódios ordenada por número do episódio (usando Comparator.comparingInt)
        // =========================================================================
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

		episodios//.stream()
				//.sorted(Comparator.comparingInt(Episodio::getNumeroEpisodio)) // Ordena por número do episódio
				.forEach(e -> System.out.println(""
						+ "Temporada " + e.getTemporada() 
						+ " - Episódio " + e.getNumeroEpisodio() 
						+ ": " + e.getTitulo() 
						+ " (Avaliação: " + e.getAvaliacao() + ")"));
				
    }
}