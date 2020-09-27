package br.com.gile.livrariaapi.service.impl;

import br.com.gile.livrariaapi.exception.BusinessException;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.model.repository.LivroRepository;
import br.com.gile.livrariaapi.service.LivroService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LivroServiceImpl implements LivroService {

    private LivroRepository repository;

    public LivroServiceImpl(LivroRepository repository) {
        this.repository = repository;
    }

    @Override
    public Livro save(Livro livro) {
        if (repository.existsByIsbn(livro.getIsbn())){
            throw new BusinessException("Isbn já cadastrado.");
        }
        return repository.save(livro);
    }

    @Override
    public Optional<Livro> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Livro livro) {

    }

    @Override
    public Livro update(Livro livro) {
        return null;
    }


}
