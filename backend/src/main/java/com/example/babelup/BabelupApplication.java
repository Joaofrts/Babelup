package com.example.babelup;

import com.example.babelup.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BabelupApplication {

	public static void main(String[] args) {

		SpringApplication.run(BabelupApplication.class, args);
	}

	@Bean
    CommandLineRunner start(UsuarioService service) {
		return args -> {
			// Aqui o Spring já injetou o service corretamente
			service.startDb();
			System.out.println("Banco de dados inicializado com sucesso!");
		};
	}

}
