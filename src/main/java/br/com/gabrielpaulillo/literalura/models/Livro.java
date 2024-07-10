package br.com.gabrielpaulillo.literalura.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    private String idioma;
    private Long numeroDownloads;


    public Livro(){}

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();

        if (dadosLivro.autores() != null && !dadosLivro.autores().isEmpty()){
            this.autor = new Autor(dadosLivro.autores().get(0)); // pega o primeiro autor da Lista de autores
        } else {
            this.autor = null;
        }

        this.idioma = tratamentoIdioma(dadosLivro.idioma());
        this.numeroDownloads = dadosLivro.numeroDownloads();
    }

    public Livro(Livro livro){}


    private String tratamentoIdioma(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Idioma não identificado.";
        } else {
            return idiomas.get(0);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Long numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    @Override
    public String toString() {
        return "\n+---------- Livro ----------+" +
                "\nTítulo: " + titulo +
                "\nAutor: " + (autor != null ? autor.getNome() : "N/A") +
                "\nIdioma: " + idioma +
                "\nNúmero de downloads:" + numeroDownloads +
                "\n+--------------------------+\n";
    }
}
