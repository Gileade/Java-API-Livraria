package br.com.gile.livrariaapi.model.repository;

import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    boolean existsByLivroAndNotRetornado(Livro livro);
}
