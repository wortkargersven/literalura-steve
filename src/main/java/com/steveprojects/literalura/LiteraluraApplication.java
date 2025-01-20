package com.steveprojects.literalura;

import com.steveprojects.literalura.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.steveprojects.literalura.model")
@EnableJpaRepositories("com.steveprojects.literalura.repositories")
@ComponentScan(basePackages = "com.steveprojects.literalura")

public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private Main main;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}


@Override
public void run(String... args) throws Exception {
	try {
		if (main != null) {
			main.showMenu();
		} else {
			System.out.println("Main is not initialised.");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}



}
