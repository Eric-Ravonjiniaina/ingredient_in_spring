package com.hei_school.ingredient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class IngredientApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngredientApplication.class, args);
	}
	@Bean
	public CommandLineRunner testConnection(DataSource dataSource) {
		return args -> {
			try (Connection conn = dataSource.getConnection()) {
				if (conn != null && !conn.isClosed()) {
					System.out.println("CONNEXION RÉUSSIE À : " + conn.getMetaData().getURL());
				}
			} catch (Exception e) {
				System.err.println("❌ ÉCHEC DE CONNEXION : " + e.getMessage());
			}
		};
	}
}
