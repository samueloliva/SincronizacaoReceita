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
	
	public void sicronizar(ReceitaService receitaService, String arg) throws RuntimeException, InterruptedException, IOException {
		// TODO: validar nome e formato do arquivo de entrada
		// abre arquivo passado por argumento	
				
		File file = new File(arg);
		FileReader fileReader = new FileReader(file);
		
		// cria uma lista com uma lista de string aninhada para armazenar os registros dos arquivos
		List<List<String>> registros = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(fileReader)) {
			// Pula a primeira linha, que corresponde às colunas
			br.readLine();
			String line = null;
			
			// Lê cada uma das próximas linhas
			while ((line = br.readLine()) != null) {
				// quebra cada valor separado por ";" e armazena cada um no array 
				String[] valores = line.split(";");
				// cria variáveis para armazenar cada valor da linha
				String agencia = valores[0];
				// elimina o "-" das contas para ficar de acordo com o ReceitaService.java
				String conta = valores[1].replace("-", "");
				// troca "," por "." e converte para decimal
				double saldo = Double.parseDouble(valores[2].replace(",", "."));
				String status = valores[3];
				
				System.out.println("Verificando ReceitaService...");
				boolean atualizado = receitaService.atualizarConta(agencia, conta, saldo, status);
				
				// Adiciona o resultado do serviço à linha
				String[] linhaAtualizada = {agencia, conta, String.valueOf(saldo), status, String.valueOf(atualizado)};

				// Atualiza os registros com a nova informação
				registros.add(Arrays.asList(linhaAtualizada));
				System.out.println("Resposta do ReceitaService: " + atualizado);
			}
			
		}
		
		// Salvando o arquivo atualizado
		System.out.println("Salvando em arquivo... ");
		String nomeArquivoSaida = arg.replace(".csv", "_updated.csv");
		FileWriter csvWriter = new FileWriter(nomeArquivoSaida);
		csvWriter.append("Agencia");
		csvWriter.append(";");
		csvWriter.append("Conta");
		csvWriter.append(";");
		csvWriter.append("Saldo");
		csvWriter.append(";");
		csvWriter.append("Status");
		csvWriter.append(";");
		csvWriter.append("Atualizado");
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
