package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.LivroDTO;
import br.com.gile.livrariaapi.api.model.entity.Livro;
import br.com.gile.livrariaapi.service.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroDTO cria(@RequestBody LivroDTO dto){
        Livro entidade =
                Livro.builder()
                    .autor(dto.getAutor())
                    .titulo(dto.getTitulo())
                    .isbn(dto.getIsbn())
                    .build();

        entidade = service.save(entidade);

        return LivroDTO.builder()
                .id(entidade.getId())
                .autor(entidade.getAutor())
                .titulo(entidade.getTitulo())
                .isbn(entidade.getIsbn())
                .build();
    }
}
