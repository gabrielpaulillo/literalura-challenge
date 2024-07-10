package br.com.gabrielpaulillo.literalura.principal;

import br.com.gabrielpaulillo.literalura.models.Autor;
import br.com.gabrielpaulillo.literalura.models.DadosLivro;
import br.com.gabrielpaulillo.literalura.models.Livro;
import br.com.gabrielpaulillo.literalura.repositories.IRepositorioAutor;
import br.com.gabrielpaulillo.literalura.repositories.IRepositorioLivro;
import br.com.gabrielpaulillo.literalura.services.ConsumoApi;
import br.com.gabrielpaulillo.literalura.services.ConverteDados;
import br.com.gabrielpaulillo.literalura.services.DadosRespostaApi;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Principal {

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://gutendex.com/books/?search=";


//    private List<Livro> listaLivros = new ArrayList<>();
    private IRepositorioLivro repositorioLivro;
    private IRepositorioAutor repositorioAutor;

    public Principal(IRepositorioLivro repositorioLivro, IRepositorioAutor repositorioAutor) {
        this.repositorioLivro = repositorioLivro;
        this.repositorioAutor = repositorioAutor;
    }

    public void exibirMenu() {
        String menu = """
                    \n+-------------------------------------------+
                    |               MENU DE OPÇÕES              |
                    +-------------------------------------------+
                    |   1 - Buscar livro por título             |
                    |   2 - Listar livros buscados              |
                    |   3 - Buscar título já registrado         |
                    |   4 - Listar autores registrados          |
                    |   5 - Buscar livro por idioma             |
                    |   6 - Buscar autores vivos em deter-      |
                    |       -minado ano                         |
                    +-------------------------------------------+
                    |   0 - Sair                                |
                    +-------------------------------------------+\n
                    +-------------------------------------------+
                    |      INSIRA A OPÇÃO DESEJADA ABAIXO:      |
                    +-------------------------------------------+
                    """;

        var opcao = -1;

        while (opcao != 0) {
            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao){
                case 1:
                    buscarLivroWeb();
                    break;
                case 2:
                    listarLivrosBuscados();
                    break;
                case 3:
                    buscarLivroPorTitulo();
                    break;
                case 4:
                    listarAutoresBuscados();
                    break;
                case 5:
                    buscarLivroPorIdioma();
                    break;
                case 6:
                    buscarAutoresVivosEmDeterminadoAno();
                    break;
                case 0:
                    System.out.println("Bye bye.");
                    break;
                default:
                    System.out.println("Tente novamente. Entrada inválida.");
            }
        }
    }

    public Livro getDadosLivro() {
        System.out.println("""
                        \n+-------------------------------------------+
                        |          INSIRA O LIVRO DESEJADO:         |
                        +-------------------------------------------+
                        """);
        var tituloInserido = scanner.nextLine();
        var json = consumoApi.obterDados(ENDERECO + tituloInserido.replace(" ", "%20"));

//        System.out.println("JSON obtido: " + json);

        DadosRespostaApi dadosRespostaApi = conversor.converterDados(json, DadosRespostaApi.class);

        if (!dadosRespostaApi.getResultadoBusca().isEmpty()){
            DadosLivro dadosLivro = dadosRespostaApi.getResultadoBusca().get(0);
//            System.out.println("Dados Livro Convertido: " + dadosLivro);
            return new Livro(dadosLivro);
        } else {
            System.out.println("\n!!! Resultado não encontrado !!!");
            return null;
        }
    }

    public void buscarLivroWeb(){

        Livro livro = getDadosLivro();

        if (livro == null) {
            System.out.println("\n>>> Título inserido não encontrado <<<");
            return;
        }

//        listaLivros.add(livro); // adicionar livro no array

        try {
            boolean livroExists = repositorioLivro.existsByTitulo(livro.getTitulo());

            if (livroExists) {
                System.out.println(">>> Livro já existe no banco de dados <<<");
            } else {
                repositorioLivro.save(livro);
                System.out.println(livro); // Sem essa impressão não acontece nada
            }
        } catch (InvalidDataAccessApiUsageException e) {
            System.out.println(">>> Livro buscado não pode ser registrado <<<");
        }
    }

    @Transactional(readOnly = true)
    public void listarLivrosBuscados() {
//        listaLivros.forEach(System.out::println); // impressão de todos os livros do array
        List<Livro> livros = repositorioLivro.findAll();
        if (livros.isEmpty()) {
            System.out.println(">>> Não foi encontrado nenhum livro no banco de dados <<<");
        } else {
            System.out.println("Livros encontrados no banco de dados:");
            livros.forEach(System.out::println);
        }

    }

    private void buscarLivroPorTitulo() {
        System.out.println("""
                        \n+-------------------------------------------+
                        |         INSIRA O TÍTULO DESEJADO:         |
                        +-------------------------------------------+
                        """);

        String titulo = scanner.nextLine();
        Livro livroBuscado = repositorioLivro.findByTituloContainsIgnoreCase(titulo);
        if (livroBuscado == null) {
            System.out.println("\n>>>" + titulo + " não foi encontrado no banco de dados <<<");
        } else {
            System.out.println("\n>>> O título buscado " + titulo + " está registrado! <<<\n");
        }
    }

    private void listarAutoresBuscados() {
        List<Autor> autores = repositorioAutor.findAll();

        if (autores.isEmpty()) {
            System.out.println(">>> Nenhum autor registrado no banco de dados <<<");
        } else {
            System.out.println(">>> Autores encontrados no banco de dados: <<<");
            Set<String> conjuntoAutores = new HashSet<>();
            for (Autor autor : autores) {
                if (conjuntoAutores.add(autor.getNome())){
                    System.out.println("\n>> " + autor.getNome());
                }
            }

        }
    }

    private void buscarLivroPorIdioma() {
        System.out.println("""
                    \n+-------------------------------------------+
                    |       SELECIONE O IDIOMA DESEJADO:        |
                    +-------------------------------------------+
                    |   → en (Livros em inglês)                 |
                    |   → pt (Livros em português)              |
                    |   → es (Livros em espanhol)               |
                    +-------------------------------------------+\n
                    """);

        String idiomaSelecionado = scanner.nextLine();
        List<Livro> livros = repositorioLivro.findByIdioma(idiomaSelecionado);

        if (livros.isEmpty()) {
            System.out.println(">>> Não foi encontrado nenhum livro no idioma selecionado! <<<");
        } else {
            System.out.println(">>> Livros encontrados no idioma '" + idiomaSelecionado + "' selecionado: <<<");
            livros.forEach(System.out::println);
        }
    }

    private void buscarAutoresVivosEmDeterminadoAno(){

        System.out.println("Informe o ano que deseja buscar: ");
        int anoInformado = scanner.nextInt();

        List<Autor> autores = repositorioAutor.buscarPorAnoInformado(anoInformado);

        if (autores.isEmpty()){
            System.out.println(">>> Não foram encontrados autores registrados nesse ano <<<");
        } else {
            System.out.println("\n>>> Autores que estavam vivos no ano " + anoInformado + ": <<<");
            for (Autor autor : autores) {
                if (autor.getAnoNascimento() != null && autor.getAnoFalecimento() != null) {
                    System.out.println(">> Autor: " + autor.getNome());
                }
            }
        }
    }
}
