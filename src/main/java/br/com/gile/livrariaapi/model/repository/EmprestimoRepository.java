package br.com.gile.livrariaapi.model.repository;

import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @Query(value = "select case when (count(e.id) > 0) then true else false end " +
            "from Emprestimo e where e.livro = :livro and (e.retornado is null or e.retornado is false)")
    boolean existsByLivroAndNotRetornado( @Param("livro") Livro livro);
}
