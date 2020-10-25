package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.EmprestimoDto;
import br.com.gile.livrariaapi.api.dto.EmprestimoRetornadoDTO;
import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.service.EmprestimoService;
import br.com.gile.livrariaapi.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("api/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {
    private final EmprestimoService service;
    private final LivroService livroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid EmprestimoDto dto){
        Livro livro = livroService
                .getByIsbn(dto.getIsbn())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Livro não encontrado para o isbn"));
        Emprestimo entidade = Emprestimo.builder()
                            .livro(livro)
                            .cliente(dto.getCliente())
                            .dataDoEmprestimo(LocalDate.now())
                            .build();

        entidade = service.save(entidade);
        return entidade.getId();
    }

    @PatchMapping("{id}")
    public void retornaLivro(@PathVariable Long id, @RequestBody EmprestimoRetornadoDTO dto){
        Emprestimo emprestimo = service.getById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        emprestimo.setRetornado(dto.getRetornado());

        service.update(emprestimo);
    }
}
