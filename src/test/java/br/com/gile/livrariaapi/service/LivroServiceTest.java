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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        when(repository.save(livro)).thenReturn(
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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

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
    public void pegaLivroPorIdTest(){
        //Cenário
        Long id = 1l;
        Livro livro = criaLivroValido();
        livro.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(livro));

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
        when(repository.findById(id)).thenReturn(Optional.empty());

        //Execução
        Optional<Livro> livro = service.getById(id);

        //Verificação
        assertThat(livro.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deletaLivroTest(){
        //Cenário
        Long id = 1l;
        Livro livro = Livro.builder().id(id).build();


        //Execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(livro));

        //Verificação
        Mockito.verify(repository, Mockito.times(1)).delete(livro);
    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar deletar um livro inexistente.")
    public void deletaLivroInvalidoTest(){
        //Cenário
        Livro livro = Livro.builder().build();


        //Execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(livro));

        //Verificação
        Mockito.verify(repository, Mockito.never()).delete(livro);
    }

    @Test
    @DisplayName("Deve atualizar um livro.")
    public void atualizaLivroTest(){
        //Cenário
        Long id = 1l;
        Livro livroAAtualizar = Livro.builder().id(id).build();
        Livro livroAtualizado = criaLivroValido();
        livroAtualizado.setId(id);
        when(repository.save(livroAAtualizar)).thenReturn(livroAtualizado);


        //Execução
        Livro livro = service.update(livroAAtualizar);

        //Verificação
        assertThat(livro.getId()).isEqualTo(livroAtualizado.getId());
        assertThat(livro.getAutor()).isEqualTo(livroAtualizado.getAutor());
        assertThat(livro.getIsbn()).isEqualTo(livroAtualizado.getIsbn());
        assertThat(livro.getTitulo()).isEqualTo(livroAtualizado.getTitulo());
    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar atualizar um livro inexistente.")
    public void atualizaLivroInvalidoTest(){
        //Cenário
        Livro livro = Livro.builder().build();


        //Execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(livro));

        //Verificação
        Mockito.verify(repository, Mockito.never()).save(livro);
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades.")
    public void procuraLivroTest(){
        //Cenário
        Livro livro = criaLivroValido();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Livro> lista = Arrays.asList(livro);
        Page<Livro> page = new PageImpl<Livro>(lista, pageRequest, 1);
        when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //Execução
        Page<Livro> resultado = service.find(livro, pageRequest);


        //Verificação
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent()).isEqualTo(lista);
        assertThat(resultado.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(resultado.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve obter um livro pelo isbn")
    public void procuraLivroPorIsbnTest(){
        String isbn = "1230";
        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Livro.builder().id(1l).isbn(isbn).build()));

        Optional<Livro> livro = service.getByIsbn(isbn);

        assertThat(livro.isPresent()).isTrue();
        assertThat(livro.get().getId()).isEqualTo(1l);
        assertThat(livro.get().getIsbn()).isEqualTo(isbn);

        verify(repository, times(1)).findByIsbn(isbn);
    }


    private Livro criaLivroValido() {
        return Livro.builder().autor("Fulano").titulo("Shadow of War").isbn("123").build();
    }
}
