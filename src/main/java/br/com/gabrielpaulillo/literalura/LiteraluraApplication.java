package br.com.gabrielpaulillo.literalura;

import br.com.gabrielpaulillo.literalura.principal.Principal;
import br.com.gabrielpaulillo.literalura.repositories.IRepositorioAutor;
import br.com.gabrielpaulillo.literalura.repositories.IRepositorioLivro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private IRepositorioLivro repositorioLivro;

	@Autowired
	private IRepositorioAutor repositorioAutor;


	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(repositorioLivro, repositorioAutor);
		principal.exibirMenu();

	}
}
