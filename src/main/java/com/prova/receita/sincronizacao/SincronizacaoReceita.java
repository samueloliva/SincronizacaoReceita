package com.prova.receita.sincronizacao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.prova.receita.service.ReceitaService;

@Controller
public class SincronizacaoReceita {
	private File file;
	private FileReader fileReader;
	
	private String colAgencia = "agencia";
	private String colConta = "conta"; 
	private String colSaldo = "saldo";
	private String colStatus = "status";
	
	public void sicronizar(ReceitaService receitaService, String arg) throws RuntimeException, InterruptedException, IOException {
		// TODO: validar nome e formato do arquivo de entrada
		// abre arquivo passado por argumento	
		
		try {
			file = new File(arg);
			fileReader = new FileReader(file);
		} catch (IOException e) {
			System.out.println("ERRO: Arquivo ou diretório não encontrado.");
			System.exit(0);
		}
						
		// cria uma lista com uma lista de string aninhada para armazenar os registros dos arquivos
		List<List<String>> registros = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(fileReader)) {
			String line;
			String[] valores;
			
			// A primeira linha corresponde às colunas
			// Verifica se o arquivo possui as quatro colunas:
			// agencia; conta; saldo; status
			line = br.readLine();
			valores = line.split(";");
			if (valores.length == 4) {
				if ( !valores[0].equalsIgnoreCase(colAgencia) ) {
					System.out.println("ERRO: A primeira coluna deve ser identificada como - " + colAgencia);
					System.exit(0);	
				}
				if ( !valores[1].equalsIgnoreCase(colConta) ) {
					System.out.println("ERRO: A segunda coluna deve ser identificada como - " + colConta);
					System.exit(0);	
				}
				if ( !valores[2].equalsIgnoreCase(colSaldo) ) {
					System.out.println("ERRO: A terceira coluna deve ser identificada como - " + colSaldo);
					System.exit(0);	
				}
				if ( !valores[3].equalsIgnoreCase(colStatus) ) {
					System.out.println("ERRO: A quarta (última) coluna deve ser identificada como - " + colStatus);
					System.exit(0);	
				}
				
			} else {
				System.out.println("Número incorreto de colunas");
				System.exit(0);
			}
			
			// Muda para a segunda linha
			br.readLine();
			
			int lineNumber = 1; 			
			while ((line = br.readLine()) != null) {
				valores = line.split(";");			
				if (valores.length != 4) {
					System.out.println("ERRO: A linha " + lineNumber + " deve ter 4 valores");
					System.exit(0);
				}
				
				String agencia = (valores[0] == null || valores[0].isEmpty()) ? null : valores[0];
				String conta = (valores[1] == null || valores[1].isEmpty()) ? null : valores[1].replace("-", "");
				double saldo = (valores[2] == null || valores[2].isEmpty()) ? 
						Double.NaN : Double.parseDouble(valores[2].replace(",", "."));				
				String status = (valores[3] == null || valores[3].isEmpty()) ? null : valores[3];
				
				System.out.println("Verificando ReceitaService...");
				boolean response = receitaService.atualizarConta(agencia, conta, saldo, status);
				String atualizado = (response == true) ? "S" : "N"; 
				// Adiciona o resultado do serviço à linha
				String[] linhaAtualizada = {agencia, conta, String.valueOf(saldo), status, String.valueOf(atualizado)};

				// Atualiza os registros com a nova informação
				registros.add(Arrays.asList(linhaAtualizada));
				System.out.println("Resposta do ReceitaService: " + atualizado);
				
				lineNumber += 1;
			}
			
		}
		
		// Salvando o arquivo atualizado
		System.out.println("Salvando em arquivo... ");
		String nomeArquivoSaida = arg.replace(".csv", "_updated.csv");
		FileWriter csvWriter = new FileWriter(nomeArquivoSaida);
		csvWriter.append(colAgencia);
		csvWriter.append(";");
		csvWriter.append(colConta);
		csvWriter.append(";");
		csvWriter.append(colSaldo);
		csvWriter.append(";");
		csvWriter.append(colStatus);
		csvWriter.append(";");
		csvWriter.append("atualizado");
		csvWriter.append("\n");


		// Criando cada linha do arquivo novo com os valores dos registros atualizados
		for (List<String> registro : registros) {
			csvWriter.append(String.join(";", registro));
			csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
	
		System.out.println("Arquivo salvo como " + nomeArquivoSaida);
		
		
	}
	
	
}
