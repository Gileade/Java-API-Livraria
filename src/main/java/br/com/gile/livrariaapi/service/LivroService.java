package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.model.entity.Livro;

import java.util.Optional;

public interface LivroService {
    Livro save(Livro livro);

    Optional<Livro> getById(Long id);

    void delete(Livro livro);

    Livro update(Livro livro);
}
