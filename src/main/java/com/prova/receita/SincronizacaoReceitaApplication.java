package com.prova.receita;

import static java.lang.System.exit;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.prova.receita.service.ReceitaService;
import com.prova.receita.sincronizacao.SincronizacaoReceita;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class SincronizacaoReceitaApplication implements CommandLineRunner {
	@Autowired
	private ReceitaService receitaService;
	
	public static void main(String[] args) {
		// desativar o banner sprint boot
		SpringApplication app = new SpringApplication(SincronizacaoReceitaApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
	
	@Override
	public void run(String...args) throws NumberFormatException, FileNotFoundException, IOException, RuntimeException, InterruptedException {
		// Verifica se o arquivo foi passado por argumento 
		if (args.length == 0 || args == null) {
			System.out.println("!!! ERRO: arquivo não encontrado !!!");
			System.out.println("Uso correto é: java -jar programa.jar <arquivo.csv>");
			System.exit(0);
		}
		
		SincronizacaoReceita sincronizacaoReceita = new SincronizacaoReceita();
		sincronizacaoReceita.sicronizar(receitaService, args[0]);
		exit(0);
	}
	

}
