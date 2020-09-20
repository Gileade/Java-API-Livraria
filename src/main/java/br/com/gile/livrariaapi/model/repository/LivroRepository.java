package br.com.gile.livrariaapi.model.repository;


import br.com.gile.livrariaapi.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
}
