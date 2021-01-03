package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.EmprestimoDto;
import br.com.gile.livrariaapi.api.dto.EmprestimoFilterDTO;
import br.com.gile.livrariaapi.api.dto.EmprestimoRetornadoDTO;
import br.com.gile.livrariaapi.api.dto.LivroDTO;
import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.service.EmprestimoService;
import br.com.gile.livrariaapi.service.LivroService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {
    private final EmprestimoService service;
    private final LivroService livroService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cria um Empréstimo")
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
    @ApiOperation("Obtém detalhes de um Empréstimo por id")
    public void retornaLivro(@PathVariable Long id, @RequestBody EmprestimoRetornadoDTO dto){
        Emprestimo emprestimo = service.getById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        emprestimo.setRetornado(dto.getRetornado());

        service.update(emprestimo);
    }

    @GetMapping
    @ApiOperation("Busca um Empréstimo por parâmetros")
    public Page<EmprestimoDto> find(EmprestimoFilterDTO dto, Pageable pageRequest){
        Page<Emprestimo> result = service.find(dto, pageRequest);
        List<EmprestimoDto> lista = result
                .getContent()
                .stream()
                .map(entity -> {
                            Livro livro = entity.getLivro();
                            LivroDTO livroDTO = modelMapper.map(livro,LivroDTO.class);
                            EmprestimoDto emprestimoDto = modelMapper.map(entity,EmprestimoDto.class);
                            emprestimoDto.setLivro(livroDTO);
                            return emprestimoDto;
                        })
                .collect(Collectors.toList());
        return new PageImpl<EmprestimoDto>(lista, pageRequest, result.getTotalElements());
    }
}
