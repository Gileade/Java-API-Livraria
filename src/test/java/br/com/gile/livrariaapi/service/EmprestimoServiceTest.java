package br.com.gile.livrariaapi.service;

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
import static org.mockito.Mockito.when;

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
    public void salvarEmprestimo(){
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

        when(repository.save(salvandoEmprestimo)).thenReturn(emprestimoSalvo);

        Emprestimo emprestimo = service.save(salvandoEmprestimo);

        assertThat(emprestimo.getId()).isEqualTo(emprestimoSalvo.getId());
        assertThat(emprestimo.getLivro().getId()).isEqualTo(emprestimoSalvo.getLivro().getId());
        assertThat(emprestimo.getCliente()).isEqualTo(emprestimoSalvo.getCliente());
        assertThat(emprestimo.getDataDoEmprestimo()).isEqualTo(emprestimoSalvo.getDataDoEmprestimo());
    }
}
