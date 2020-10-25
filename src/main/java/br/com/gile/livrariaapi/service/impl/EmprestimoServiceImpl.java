package br.com.gile.livrariaapi.service.impl;

import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.repository.EmprestimoRepository;
import br.com.gile.livrariaapi.service.EmprestimoService;

public class EmprestimoServiceImpl implements EmprestimoService {
    private EmprestimoRepository repository;

    public EmprestimoServiceImpl(EmprestimoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Emprestimo save(Emprestimo emprestimo) {
        return repository.save(emprestimo);
    }
}

