package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.EmprestimoDto;
import br.com.gile.livrariaapi.api.dto.LivroDTO;
import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.service.EmprestimoService;
import br.com.gile.livrariaapi.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService service;
    private final ModelMapper modelMapper;
    private final EmprestimoService emprestimoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroDTO create(@RequestBody @Valid LivroDTO dto){
        Livro entidade = modelMapper.map(dto, Livro.class);
        entidade = service.save(entidade);
        return modelMapper.map(entidade, LivroDTO.class);
    }

    @GetMapping("{id}")
    public LivroDTO get(@PathVariable Long id){
        return service
                .getById(id)
                .map(livro -> modelMapper.map(livro,LivroDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Livro livro = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(livro);
    }

    @PutMapping("{id}")
    public LivroDTO update(@PathVariable Long id, LivroDTO dto){
        return service.getById(id).map(livro -> {
            livro.setAutor(dto.getAutor());
            livro.setTitulo(dto.getTitulo());
            livro = service.update(livro);
            return modelMapper.map(livro, LivroDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    public Page<LivroDTO> find(LivroDTO dto, Pageable pageRequest){
        Livro filtro = modelMapper.map(dto,Livro.class);
        Page<Livro> resultado = service.find(filtro,pageRequest);
        List<LivroDTO> lista = resultado.getContent().stream()
                .map(entity -> modelMapper.map(entity, LivroDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<LivroDTO>(lista, pageRequest, resultado.getTotalElements());
    }

    @GetMapping("{id}/emprestimos")
    public Page<EmprestimoDto> emprestimosPorLivro(@PathVariable Long id, Pageable pageable){
        Livro livro = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<Emprestimo> resultado = emprestimoService.getEmprestimoPorLivro(livro, pageable);
        List<EmprestimoDto> list = resultado.getContent()
                .stream()
                .map(emprestimo -> {
                    Livro emprestimoLivro = emprestimo.getLivro();
                    LivroDTO livroDTO = modelMapper.map(emprestimoLivro, LivroDTO.class);
                    EmprestimoDto emprestimoDto = modelMapper.map(emprestimo, EmprestimoDto.class);
                    emprestimoDto.setLivro(livroDTO);
                    return emprestimoDto;
                }).collect(Collectors.toList());
        return new PageImpl<EmprestimoDto>(list,pageable,resultado.getTotalElements());
    }
}
