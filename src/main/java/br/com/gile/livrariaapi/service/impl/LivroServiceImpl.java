package br.com.gile.livrariaapi.service.impl;

import br.com.gile.livrariaapi.api.model.entity.Livro;
import br.com.gile.livrariaapi.exception.BusinessException;
import br.com.gile.livrariaapi.model.repository.livroRepository;
import br.com.gile.livrariaapi.service.LivroService;
import org.springframework.stereotype.Service;

@Service
public class LivroServiceImpl implements LivroService {

    private livroRepository repository;

    public LivroServiceImpl(livroRepository repository) {
        this.repository = repository;
    }

    @Override
    public Livro save(Livro livro) {
        if (repository.existsByIsbn(livro.getIsbn())){
            throw new BusinessException("Isbn já cadastrado.");
        }
        return repository.save(livro);
    }
}
