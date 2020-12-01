package br.com.gile.livrariaapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoDto {

    private Long id;
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String cliente;
    @NotEmpty
    private String email;
    private LivroDTO livro;
}
