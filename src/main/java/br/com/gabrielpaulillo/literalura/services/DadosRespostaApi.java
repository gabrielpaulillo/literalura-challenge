package br.com.gabrielpaulillo.literalura.services;

import br.com.gabrielpaulillo.literalura.models.DadosLivro;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosRespostaApi {

    @JsonAlias("results")
    List<DadosLivro> resultadoBusca;

    public List<DadosLivro> getResultadoBusca() {
        return resultadoBusca;
    }

    public void setResultadoBusca(List<DadosLivro> resultadoBusca) {
        this.resultadoBusca = resultadoBusca;
    }
}
