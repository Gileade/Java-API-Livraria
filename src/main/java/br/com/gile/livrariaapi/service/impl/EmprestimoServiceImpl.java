package br.com.gile.livrariaapi.service.impl;

import br.com.gile.livrariaapi.api.dto.EmprestimoFilterDTO;
import br.com.gile.livrariaapi.exception.BusinessException;
import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.repository.EmprestimoRepository;
import br.com.gile.livrariaapi.service.EmprestimoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class EmprestimoServiceImpl implements EmprestimoService {
    private EmprestimoRepository repository;

    public EmprestimoServiceImpl(EmprestimoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Emprestimo save(Emprestimo emprestimo) {
        if (repository.existsByLivroAndNotRetornado(emprestimo.getLivro())){
            throw new BusinessException("Livro já emprestado");
        }
        return repository.save(emprestimo);
    }

    @Override
    public Optional<Emprestimo> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Emprestimo update(Emprestimo emprestimo) {
        return repository.save(emprestimo);
    }

    @Override
    public Page<Emprestimo> find(EmprestimoFilterDTO filterDTO, Pageable pageable) {
        return repository.findByLivroIsbnOrCliente(filterDTO.getIsbn(),filterDTO.getCliente(),pageable);
    }
}

