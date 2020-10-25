package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.api.resource.LivroController;
import br.com.gile.livrariaapi.model.entity.Emprestimo;

import java.util.Optional;

public interface EmprestimoService {
    Emprestimo save(Emprestimo emprestimo);

    Optional<Emprestimo> getById(Long id);

    Emprestimo update(Emprestimo emprestimo);
}
