package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.api.model.entity.Livro;
import br.com.gile.livrariaapi.model.repository.livroRepository;
import br.com.gile.livrariaapi.service.impl.LivroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LivroServiceTest {

    LivroService service;

    @MockBean
    livroRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new LivroServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void salvarLivroTest(){
        //Cenário
        Livro livro = Livro.builder().autor("Fulano").titulo("Shadow of War").isbn("123").build();
        Mockito.when(repository.save(livro)).thenReturn(
                Livro.builder()
                        .id(1l)
                        .isbn("123")
                        .titulo("Shadow of War")
                        .autor("Fulano")
                        .build()
        );

        //Execução
        Livro livroSalvo = service.save(livro);

        //Verificação
        assertThat(livroSalvo.getId()).isNotNull();
        assertThat(livroSalvo.getIsbn()).isEqualTo("123");
        assertThat(livroSalvo.getTitulo()).isEqualTo("Shadow of War");
        assertThat(livroSalvo.getAutor()).isEqualTo("Fulano");
    }
}
