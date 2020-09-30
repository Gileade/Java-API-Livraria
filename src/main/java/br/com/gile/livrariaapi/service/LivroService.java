package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.model.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LivroService {
    Livro save(Livro livro);

    Optional<Livro> getById(Long id);

    void delete(Livro livro);

    Livro update(Livro livro);

    Page<Livro> find(Livro filter, Pageable pageRequest);
}
