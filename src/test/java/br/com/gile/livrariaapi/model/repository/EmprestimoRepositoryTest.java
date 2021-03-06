package br.com.gile.livrariaapi.model.repository;

import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static br.com.gile.livrariaapi.model.repository.LivroRepositoryTest.criaNovoLivro;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class EmprestimoRepositoryTest {

    @Autowired
    private EmprestimoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verificar se existe empréstimo não devolvido para o livro.")
    public void existsByLivroAndNotRetornado(){
        //Cenário
        Emprestimo emprestimo = criaEGravaEmprestimo(LocalDate.now());
        Livro livro = emprestimo.getLivro();

        //Execução
        boolean exists = repository.existsByLivroAndNotRetornado(livro);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve buscar empréstimo pelo isbn do livro ou cliente")
    public void procuraLivroPorIsbnOuClienteTest(){
        //Cenário
        Emprestimo emprestimo = criaEGravaEmprestimo(LocalDate.now());

        Page<Emprestimo> resultado = repository.findByLivroIsbnOrCliente("123", "Fulano", PageRequest.of(0, 10));

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent()).contains(emprestimo);
        assertThat(resultado.getPageable().getPageSize()).isEqualTo(10);
        assertThat(resultado.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(resultado.getTotalElements()).isEqualTo(1);

    }

    @Test
    @DisplayName("Deve obter epréstimos cuja data de emprestimo for menor ou igual a tres dias atras e não retornados")
    public void findPorDataMenorENaoRetornado(){
        //Cenário
        Emprestimo emprestimo = criaEGravaEmprestimo(LocalDate.now().minusDays(5));

        List<Emprestimo> resultado = repository.findByDataMenorENaoRetornado(LocalDate.now().minusDays(4));

        assertThat(resultado).hasSize(1);
        assertThat(resultado).contains(emprestimo);
    }

    @Test
    @DisplayName("Deve retornar vazio quando não houver emprestimos atrasados")
    public void notFindPorDataMenorENaoRetornado(){
        //Cenário
        Emprestimo emprestimo = criaEGravaEmprestimo(LocalDate.now());

        List<Emprestimo> resultado = repository.findByDataMenorENaoRetornado(LocalDate.now().minusDays(4));

        assertThat(resultado).isEmpty();
    }

    public Emprestimo criaEGravaEmprestimo(LocalDate dataEmprestimo){
        Livro livro = criaNovoLivro("123");
        entityManager.persist(livro);

        Emprestimo emprestimo = Emprestimo.builder().livro(livro).cliente("Fulano").dataDoEmprestimo(dataEmprestimo).build();
        entityManager.persist(emprestimo);

        return emprestimo;
    }
}
