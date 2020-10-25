package br.com.gile.livrariaapi.model.repository;

import br.com.gile.livrariaapi.model.entity.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LivroRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LivroRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado.")
    public void retornaVerdadeiroQuandoExisteIsbn(){
        //Cenário
        String isbn = "123";
        Livro livro = criaNovoLivro(isbn);
        entityManager.persist(livro);

        //Execução
        boolean existe = repository.existsByIsbn(isbn);

        //Verificação
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado.")
    public void retornaFalsoQuandoExisteIsbn(){
        //Cenário
        String isbn = "123";

        //Execução
        boolean existe = repository.existsByIsbn(isbn);

        //Verificação
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id.")
    public void pegaLivroPorIdTest(){
        //Cenário
        Livro livro = criaNovoLivro("123");
        entityManager.persist(livro);

        //Execução
        Optional<Livro> livroEncontrado = repository.findById(livro.getId());

        //Verificação
        assertThat(livroEncontrado.isPresent()).isTrue();
    }

    public static Livro criaNovoLivro(String isbn) {
        return Livro.builder().titulo("God of War").autor("Fulano").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve salvar um livro.")
    public void salvaLivroTest(){
        //Cenário
        Livro livro = criaNovoLivro("123");

        //Execução
        Livro livroSalvo = repository.save(livro);

        //Verificação
        assertThat(livroSalvo.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deletaLivroTest(){
        //Cenário
        Livro livro = criaNovoLivro("123");
        entityManager.persist(livro);

        Livro livroEncontrado = entityManager.find(Livro.class, livro.getId());

        //Execução
        repository.delete(livroEncontrado);

        //Verificação
        Livro livroDeletado = entityManager.find(Livro.class, livro.getId());
        assertThat(livroDeletado).isNull();
    }
}
