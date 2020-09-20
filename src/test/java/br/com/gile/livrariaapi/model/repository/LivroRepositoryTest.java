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
        Livro livro = Livro.builder().titulo("God of War").autor("Fulano").isbn(isbn).build();
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
}
