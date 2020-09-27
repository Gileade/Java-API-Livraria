package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.exception.BusinessException;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.model.repository.LivroRepository;
import br.com.gile.livrariaapi.service.impl.LivroServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LivroServiceTest {

    LivroService service;

    @MockBean
    LivroRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new LivroServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void salvarLivroTest(){
        //Cenário
        Livro livro = criaLivroValido();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
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


    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado.")
    public void naoDeveSalvarLivroComIsbnDuplicado(){
        //Cenário
        Livro livro = criaLivroValido();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //Execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(livro));

        //Verificação
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(livro);
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void pegaPorIdTest(){
        //Cenário
        Long id = 1l;
        Livro livro = criaLivroValido();
        livro.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(livro));

        //Execução
        Optional<Livro> livroEncontrado = service.getById(id);

        //Verificação
        assertThat(livroEncontrado.isPresent()).isTrue();
        assertThat(livroEncontrado.get().getId()).isEqualTo(id);
        assertThat(livroEncontrado.get().getAutor()).isEqualTo(livro.getAutor());
        assertThat(livroEncontrado.get().getIsbn()).isEqualTo(livro.getIsbn());
        assertThat(livroEncontrado.get().getTitulo()).isEqualTo(livro.getTitulo());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por Id quando ele não existe na base")
    public void livroNaoEncontradoPorIdTest(){
        //Cenário
        Long id = 1l;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //Execução
        Optional<Livro> livro = service.getById(id);

        //Verificação
        assertThat(livro.isPresent()).isFalse();
    }


    private Livro criaLivroValido() {
        return Livro.builder().autor("Fulano").titulo("Shadow of War").isbn("123").build();
    }
}
