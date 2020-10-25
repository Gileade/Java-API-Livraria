package br.com.gile.livrariaapi.model.repository;


import br.com.gile.livrariaapi.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);

    Optional<Livro> findByIsbn(String isbn);
}
