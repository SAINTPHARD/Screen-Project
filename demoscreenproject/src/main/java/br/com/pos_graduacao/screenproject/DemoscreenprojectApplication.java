package br.com.pos_graduacao.screenproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.pos_graduacao.screenproject.model.DadosSerie;
import br.com.pos_graduacao.screenproject.service.ConsumoAPI;
import br.com.pos_graduacao.screenproject.service.ConverteDados;

@SpringBootApplication
public class DemoscreenprojectApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoscreenprojectApplication.class, args);
	}

  // Primeira impressão no console
  @Override
  public void run(String... args) throws Exception {
	  
	  // 1.Criando variável do tipo ConsumoAPI para consumir a API
	  var consumoAPI = new ConsumoAPI();
	  
	  /**
	   * 2. Chamando o método obterDados da classe ConsumoAPI, passando a URL da API como argumento e armazenando o resultado em uma variável do tipo String chamada json.
	   * A URL fornecida é "https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c", que é um endpoint da API OMDB para obter informações sobre a série "Gilmore
	   */
	  var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
	  
	  // 3.Converter o JSON em um Objeto java mapeado pela classe DadosSerie usando a classe ConverteDados
	  DadosSerie dados = new ConverteDados().obterDados(json, DadosSerie.class);
	  
	  // 4. Imprimir o resumo concatenado dos dados da série no console usando o método obterResumoConcatenado da classe DadosSerie, que organiza as informações de forma legível e estruturada.
	  System.out.println("\n=== DADOS CONCATENADOS DA SÉRIE ===");
	  System.out.println(dados.obterResumoConcatenado());
	  System.out.println("===================================\n");
  }
}
