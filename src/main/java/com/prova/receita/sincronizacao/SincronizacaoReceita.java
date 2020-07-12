package com.prova.receita.sincronizacao;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.prova.receita.service.ReceitaService;

public class SincronizacaoReceita {	
	private String colunaAgencia = "agencia";
	private String colunaConta = "conta"; 
	private String colunaSaldo = "saldo";
	private String colunaStatus = "status";
	
	public void sicronizar(ReceitaService receitaService, String arg) {
		List<String> lista = lerArquivoComoLista(arg);
		List<List<String>> registros = new ArrayList<>();
		Iterator<String> itr = lista.iterator();
		String linha;
		String[] colunas;
		String[] valores;
				
		linha = itr.next();
		colunas = linha.split(";");
		if (colunas.length == 4) {
			if ( !colunas[0].equalsIgnoreCase(colunaAgencia) ) {
				System.out.println("ERRO: A primeira coluna deve ser identificada como - " + colunaAgencia);
				System.exit(0);	
			}
			if ( !colunas[1].equalsIgnoreCase(colunaConta) ) {
				System.out.println("ERRO: A segunda coluna deve ser identificada como - " + colunaConta);
				System.exit(0);	
			}
			if ( !colunas[2].equalsIgnoreCase(colunaSaldo) ) {
				System.out.println("ERRO: A terceira coluna deve ser identificada como - " + colunaSaldo);
				System.exit(0);	
			}
			if ( !colunas[3].equalsIgnoreCase(colunaStatus) ) {
				System.out.println("ERRO: A quarta (última) coluna deve ser identificada como - " + colunaStatus);
				System.exit(0);	
			}
			
		} else {
			System.out.println("As colunas (agencia, conta, saldo, status) não foram definidas corretamente");
			System.exit(0);
		}
		
		int lineNumber = 1;
		if (itr.hasNext() == false) { System.out.println("AVISO: Tabela vazia"); }
		while (itr.hasNext()) {
			linha = itr.next();	
			valores = linha.split(";");			
			if (valores.length != 4) {
				System.out.println("ERRO: A linha " + lineNumber + " deve ter 4 valores");
				System.exit(0);
			}
			//System.out.println("SALDO:" + valores[2]);
			String agencia = (valores[0] == null || valores[0].isEmpty()) ? null : valores[0];
			String conta = (valores[1] == null || valores[1].isEmpty()) ? null : valores[1].replace("-", "");
			double saldo = (valores[2] == null || valores[2].isEmpty()) ? 
					Double.NaN : Double.parseDouble(valores[2].replace(",", "."));				
			String status = (valores[3] == null || valores[3].isEmpty()) ? null : valores[3];
			
			System.out.println("Verificando ReceitaService...");
			boolean resposta = false;
			try {
				resposta = receitaService.atualizarConta(agencia, conta, saldo, status);
			} catch (RuntimeException e) {
				System.out.println("");
				//e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("");
				//e.printStackTrace();
			}
			String atualizado = (resposta == true) ? "S" : "N"; 
			// Adiciona o resultado do serviço à linha
			String[] linhaAtualizada = {agencia, conta, String.valueOf(saldo), status, String.valueOf(atualizado)};

			// Atualiza os registros com a nova informação
			registros.add(Arrays.asList(linhaAtualizada));
			System.out.println("Resposta do ReceitaService: " + atualizado);
			
			lineNumber += 1;
		}
		
		// Salvando o arquivo atualizado
		System.out.println("Salvando em arquivo... ");
		String nomeArquivoSaida = arg.replace(".csv", "_updated.csv");
		FileWriter csvWriter;
		try {
			csvWriter = new FileWriter(nomeArquivoSaida);
			csvWriter.append(colunaAgencia);
			csvWriter.append(";");
			csvWriter.append(colunaConta);
			csvWriter.append(";");
			csvWriter.append(colunaSaldo);
			csvWriter.append(";");
			csvWriter.append(colunaStatus);
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
		} catch (IOException e) {
			System.out.println("Falha ao escrever aquivo");
			e.printStackTrace();
		}
		System.out.println("Arquivo salvo como " + nomeArquivoSaida);
	}
	
	public static List<String> lerArquivoComoLista( String fileName ) 
	{
		List <String> linhas = Collections.emptyList();
		try {
			linhas = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Failha ao abrir o arquivo");
			// e.printStackTrace();
		}
		return linhas;
	}
}
