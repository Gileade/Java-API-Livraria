package br.com.gile.livrariaapi.model.repository;

import br.com.gile.livrariaapi.model.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
}
