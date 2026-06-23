package br.com.pos_graduacao.screenproject.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        // ETAPA 1: BUSCA DE DADOS NA API E TRATAMENTO DE ERROS
        // =========================================================================
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println("\n--- DADOS DA SÉRIE ---");
        System.out.println(dados);

        // Proteção contra digitação incorreta. Se a API retornar nulo, evita o NullPointerException.
        if (dados.totalTemporadas() == null) {
            System.out.println("\n❌ Erro: Série não encontrada! Verifique se o nome foi digitado corretamente.");
            return;
        }

        // Loop imperativo tradicional para buscar todas as temporadas disponíveis
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        // =========================================================================
        // ETAPA 2: TRABALHANDO COM COLEÇÕES, LAMBDAS E METHOD REFERENCES
        // =========================================================================
        System.out.println("\n--- LISTA DE TEMPORADAS (COMPLETA) ---");
        temporadas.forEach(System.out::println);

        System.out.println("\n--- TÍTULOS DOS EPISÓDIOS (LAMBDA TRADICIONAL) ---");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        // =========================================================================
        // ETAPA 3: EVOLUÇÃO PARA OPERAÇÕES ENCADEADAS COM STREAMS (TOP 5)
        // =========================================================================
        System.out.println("\n--- TOP 5 EPISÓDIOS COM MAIOR AVALIAÇÃO ---");
        temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted((e1, e2) -> Double.compare(Double.parseDouble(e2.avaliacao()), Double.parseDouble(e1.avaliacao())))
                .limit(5)
                .forEach(e -> System.out.println(e.titulo() + " - Nota: " + e.avaliacao()));

        // =========================================================================
        // ETAPA 4: MAPEAMENTO PARA A CLASSE DE MODELO EPISODIO
        // =========================================================================
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        System.out.println("\n--- LISTAGEM COMPLETA DOS EPISÓDIOS ---");
        episodios.forEach(System.out::println);

        // ============================================================================
        // ETAPA 5: BUSCA DE EPISÓDIOS A PARTIR DE UM ANO (PADRÃO BRASIL DE EXIBIÇÃO)
        // ============================================================================
        System.out.println("\nA partir de que ano deseja ver os episódios?");
        var anoBusca = leitura.nextInt();
        leitura.nextLine(); // Limpa o buffer do teclado após ler um número inteiro

        // Criamos um objeto LocalDate apontando para o dia 1º de Janeiro do ano digitado
        LocalDate dataBusca = LocalDate.of(anoBusca, 1, 1);

        // Definimos o formatador brasileiro para a exibição final no console
        DateTimeFormatter formatadorBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("\n--- EPISÓDIOS LANÇADOS A PARTIR DE " + anoBusca + " ---");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null &&
                        (e.getDataLancamento().isAfter(dataBusca) || e.getDataLancamento().isEqual(dataBusca)))
                .forEach(e -> {
                    String dataExibicao = e.getDataLancamento().format(formatadorBR);

                    System.out.println(
                            "Temporada " + e.getTemporada()
                                    + " - Episódio " + e.getNumeroEpisodio()
                                    + ": " + e.getTitulo()
                                    + " (Avaliação: " + e.getAvaliacao() + ")"
                                    + " - Lançamento: " + dataExibicao
                    );
                }); // <- Fecha a expressão lambda do forEach
    } // <- Fecha o método exibirMenu()
} //Header: Fecha a classe Principal de forma correta