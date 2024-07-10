package br.com.gabrielpaulillo.literalura.repositories;

import br.com.gabrielpaulillo.literalura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IRepositorioAutor extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.anoFalecimento >= :anoInformado AND :anoInformado >= a.anoNascimento")
    List<Autor> buscarPorAnoInformado(int anoInformado);

    Optional<Autor> findFirstByNomeContainsIgnoreCase(String autor);
}
