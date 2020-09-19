package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.api.model.entity.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LivroServiceTest {

    LivroService service;

    @Test
    @DisplayName("Deve salvar um livro")
    public void salvarLivroTest(){
        //Cenário
        Livro livro = Livro.builder().autor("Fulano").titulo("Shadow of War").isbn("123").build();

        //Execução
        Livro livroSalvo = service.save(livro);

        //Verificação
        assertThat(livroSalvo.getId()).isNotNull();
        assertThat(livroSalvo.getIsbn()).isEqualTo("123");
        assertThat(livroSalvo.getTitulo()).isEqualTo("Shadow of War");
        assertThat(livroSalvo.getAutor()).isEqualTo("Fulano");
    }
}
