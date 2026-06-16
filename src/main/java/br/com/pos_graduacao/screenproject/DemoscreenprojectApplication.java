package br.com.pos_graduacao.screenproject;

import br.com.pos_graduacao.screenproject.model.DadosEpisodio;
import br.com.pos_graduacao.screenproject.model.DadosSerie;
import br.com.pos_graduacao.screenproject.model.DadosTemporada;
import br.com.pos_graduacao.screenproject.principal.Principal;
import br.com.pos_graduacao.screenproject.service.ConsumoAPI;
import br.com.pos_graduacao.screenproject.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @SpringBootApplication: Inicializa o Spring Boot, ativando a configuração automática
 * e a varredura de componentes (component scan) no pacote atual e subpacotes.
 * * implements CommandLineRunner: Interface que obriga a implementação do método run().
 * Ela faz com que o código dentro do run() seja executado logo após a inicialização do Spring.
 */
@SpringBootApplication
public class DemoscreenprojectApplication implements CommandLineRunner {

    // Constantes com a URL base da API do OMDB e a chave de acesso (API Key)
    private static final String URL_BASE = "https://www.omdbapi.com/?t=gilmore+girls";
    private static final String API_KEY = "&apikey=6585022c";

    // Atributos finais que representam as dependências gerenciadas pelo Spring
    private final ConsumoAPI consumoAPI;
    private final ConverteDados conversor;

    /**
     * Construtor da classe (Injeção de Dependência).
     * O Spring injeta automaticamente as instâncias de ConsumoAPI e ConverteDados aqui.
     */
    public DemoscreenprojectApplication(ConsumoAPI consumoAPI, ConverteDados conversor) {
        this.consumoAPI = consumoAPI;
        this.conversor = conversor;
    }

    /**
     * Método Main: Ponto de partida padrão de qualquer aplicação Java/Spring Boot.
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoscreenprojectApplication.class, args);
    }

    /**
     * Método run: Executado automaticamente assim que o Spring Boot termina de subir.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== INICIANDO O PROCESSAMENTO DO SCREEN PROJECT ===\n");

        // INSTÂNCIA DO MENU INTERATIVO (O fluxo atual do seu sistema)
        // Cria o menu do terminal para o usuário digitar a série desejada
        Principal principal = new Principal();
        principal.exibirMenu();

        // ----------------------------------------------------------------------------------
        // NOTA: Os métodos abaixo (buscarDadosSerie, buscarDadosEpisodio e buscarTodasTemporadas)
        // foram criados para testes automatizados com a série fixa "Gilmore Girls".
        // Eles estão comentados no fluxo do run() para priorizar o menu interativo (Principal).
        // ----------------------------------------------------------------------------------

        // DadosSerie dadosSerie = buscarDadosSerie();
        // buscarDadosEpisodio();
        // if (dadosSerie != null) {
        //     buscarTodasTemporadas(dadosSerie.totalTemporadas());
        // }

        System.out.println("\n=== PROCESSAMENTO FINALIZADO ===");
    }

    /**
     * Realiza a busca dos dados gerais da série fixa
     */
    private DadosSerie buscarDadosSerie() {
        // Envia a requisição HTTP e armazena o JSON bruto recebido
        String json = consumoAPI.obterDados(URL_BASE + API_KEY);
        // Converte o JSON para o Record Java (DadosSerie)
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        System.out.println("\n--- DADOS DA SÉRIE ---");
        System.out.println(dados);
        return dados;
    }

    /**
     * Realiza a busca dos dados de um episódio específico (Temporada 1, Episódio 2).
     */
    private void buscarDadosEpisodio() {
        // Monta a URL inserindo os parâmetros da temporada e do episódio
        String urlEpisodio = URL_BASE + "&season=1&episode=2" + API_KEY;
        String json = consumoAPI.obterDados(urlEpisodio);
        DadosEpisodio episodio = conversor.obterDados(json, DadosEpisodio.class);

        System.out.println("\n--- DADOS DO EPISÓDIO ---");
        System.out.println(episodio);
    }

    /**
     * Varre dinamicamente todas as temporadas disponíveis da série usando um laço de repetição.
     * @param totalTemporadas Número total de temporadas retornado pela API.
     */
    private void buscarTodasTemporadas(int totalTemporadas) {
        System.out.println("\n--- LISTANDO TEMPORADAS ---");
        List<DadosTemporada> temporadas = new ArrayList<>();

        // O loop roda de 1 até o total de temporadas da série para alimentar a lista
        for (int i = 1; i <= totalTemporadas; i++) {
            // Concatena o número do índice 'i' dinamicamente no parâmetro &season=
            String urlTemporada = URL_BASE + "&season=" + i + API_KEY;
            String json = consumoAPI.obterDados(urlTemporada);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

            // Adiciona a temporada convertida na lista de memória
            temporadas.add(dadosTemporada);
        }

        System.out.println("\n--- TÍTULOS DOS EPISÓDIOS EXIBIDOS VIA FOREACH ANINHADO ---");
        // Aplicação dos Lambdas aninhados para exibir os títulos de forma limpa
        temporadas.forEach(t ->
                t.episodios().forEach(e -> System.out.println(e.titulo()))
        );
    }
}