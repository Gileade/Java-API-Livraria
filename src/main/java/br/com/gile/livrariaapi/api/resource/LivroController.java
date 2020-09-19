package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.LivroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroDTO cria(){
        return null;
    }
}
