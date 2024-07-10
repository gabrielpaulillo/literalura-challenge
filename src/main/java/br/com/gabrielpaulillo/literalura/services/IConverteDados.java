package br.com.gabrielpaulillo.literalura.services;

public interface IConverteDados {
    // método genérico que pega os dados no formato JSON e converte em dados java.
    // é genérico, pois poderá servir para mais de um tipo de retorno

    <T> T converterDados(String json, Class<T> classe);

}
