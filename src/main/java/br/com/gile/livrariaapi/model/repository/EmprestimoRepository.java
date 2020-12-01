package br.com.gile.livrariaapi.model.repository;

import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @Query(value = "select case when (count(e.id) > 0) then true else false end " +
            "from Emprestimo e where e.livro = :livro and (e.retornado is null or e.retornado is false)")
    boolean existsByLivroAndNotRetornado( @Param("livro") Livro livro);

    @Query(value = "select e from Emprestimo e join e.livro as l where l.isbn = :isbn or e.cliente = :cliente")
    Page<Emprestimo> findByLivroIsbnOrCliente(
            @Param("isbn") String isbn,
            @Param("cliente") String cliente,
            Pageable pageRequest
    );

    Page<Emprestimo> findByLivro(Livro livro, Pageable pageable);
}
