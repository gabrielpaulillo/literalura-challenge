package br.com.gabrielpaulillo.literalura.repositories;

import br.com.gabrielpaulillo.literalura.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRepositorioLivro extends JpaRepository<Livro, Long> {

    boolean existsByTitulo(String titulo);

    Livro findByTituloContainsIgnoreCase(String titulo);

    List<Livro> findByIdioma(String idioma);

    @Query("SELECT l FROM Livro l ORDER BY l.numeroDownloads DESC LIMIT 10")
    List<Livro> findTop10ByNumeroDownloads();
}
