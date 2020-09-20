package br.com.gile.livrariaapi.model.repository;

import br.com.gile.livrariaapi.api.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface livroRepository extends JpaRepository<Livro, Long> {
}
