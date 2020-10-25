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
        Livro livro = Livro.builder().id(1l).build();
        String cliente = "Fulano";

        Emprestimo salvandoEmprestimo = Emprestimo.builder()
                .livro(livro)
                .cliente(cliente)
                .dataDoEmprestimo(LocalDate.now())
                .build();

        when(repository.existsByLivroAndNotRetornado(livro)).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.save(salvandoEmprestimo));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Livro já emprestado");

        verify(repository, never()).save(salvandoEmprestimo);
    }
}
