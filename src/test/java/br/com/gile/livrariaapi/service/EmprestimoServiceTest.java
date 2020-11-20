package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.exception.BusinessException;
import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.model.repository.EmprestimoRepository;
import br.com.gile.livrariaapi.service.impl.EmprestimoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EmprestimoServiceTest {

    private EmprestimoService service;

    @MockBean
    EmprestimoRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new EmprestimoServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void salvarEmprestimoTest(){
        Livro livro = Livro.builder().id(1l).build();
        String cliente = "Fulano";

        Emprestimo salvandoEmprestimo = Emprestimo.builder()
                .livro(livro)
                .cliente(cliente)
                .dataDoEmprestimo(LocalDate.now())
                .build();

        Emprestimo emprestimoSalvo = Emprestimo.builder()
                .id(1l)
                .dataDoEmprestimo(LocalDate.now())
                .cliente(cliente)
                .livro(livro)
                .build();

        when(repository.existsByLivroAndNotRetornado(livro)).thenReturn(false);
        when(repository.save(salvandoEmprestimo)).thenReturn(emprestimoSalvo);

        Emprestimo emprestimo = service.save(salvandoEmprestimo);

        assertThat(emprestimo.getId()).isEqualTo(emprestimoSalvo.getId());
        assertThat(emprestimo.getLivro().getId()).isEqualTo(emprestimoSalvo.getLivro().getId());
        assertThat(emprestimo.getCliente()).isEqualTo(emprestimoSalvo.getCliente());
        assertThat(emprestimo.getDataDoEmprestimo()).isEqualTo(emprestimoSalvo.getDataDoEmprestimo());
    }

    @Test
    @DisplayName("Deve lançar erro de negócio salvar um empréstimo com livro já emprestado")
    public void livroEmprestadosalvarEmprestimoTest(){
        Emprestimo emprestimo = criaEmprestimo();

        when(repository.existsByLivroAndNotRetornado(emprestimo.getLivro())).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.save(emprestimo));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Livro já emprestado");

        verify(repository, never()).save(emprestimo);
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo pelo ID")
    public void getEmprestimoTest(){
        Long id = 1l;

        Emprestimo emprestimo = criaEmprestimo();
        emprestimo.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(emprestimo));

        Optional<Emprestimo> resultado = service.getById(id);

        assertThat(resultado.isPresent()).isTrue();
        assertThat(resultado.get().getId()).isEqualTo(id);
        assertThat(resultado.get().getCliente()).isEqualTo(emprestimo.getCliente());
        assertThat(resultado.get().getLivro()).isEqualTo(emprestimo.getLivro());
        assertThat(resultado.get().getDataDoEmprestimo()).isEqualTo(emprestimo.getDataDoEmprestimo());

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo.")
    public void atualizaEmprestimo(){
        Long id = 1l;

        Emprestimo emprestimo = criaEmprestimo();
        emprestimo.setId(id);
        emprestimo.setRetornado(true);

        when(repository.save(emprestimo)).thenReturn(emprestimo);

        Emprestimo emprestimoAtualizado = service.update(emprestimo);

        assertThat(emprestimoAtualizado.getRetornado()).isTrue();

        verify(repository).save(emprestimo);
    }

    public Emprestimo criaEmprestimo(){
        Livro livro = Livro.builder().id(1l).build();
        String cliente = "Fulano";

        return Emprestimo.builder()
                .livro(livro)
                .cliente(cliente)
                .dataDoEmprestimo(LocalDate.now())
                .build();
    }
}
