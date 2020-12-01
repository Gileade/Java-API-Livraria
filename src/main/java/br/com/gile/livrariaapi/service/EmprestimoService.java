package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.api.dto.EmprestimoFilterDTO;
import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmprestimoService {
    Emprestimo save(Emprestimo emprestimo);

    Optional<Emprestimo> getById(Long id);

    Emprestimo update(Emprestimo emprestimo);

    Page<Emprestimo> find(EmprestimoFilterDTO filterDTO, Pageable pageable);

    Page<Emprestimo> getEmprestimoPorLivro(Livro livro, Pageable pageable);
}
