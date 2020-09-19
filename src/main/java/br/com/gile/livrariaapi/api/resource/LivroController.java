package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.LivroDTO;
import br.com.gile.livrariaapi.api.model.entity.Livro;
import br.com.gile.livrariaapi.service.LivroService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private LivroService service;
    private ModelMapper modelMapper;

    public LivroController(LivroService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroDTO cria(@RequestBody LivroDTO dto){
        Livro entidade = modelMapper.map(dto, Livro.class);
        entidade = service.save(entidade);
        return modelMapper.map(entidade, LivroDTO.class);
    }
}
