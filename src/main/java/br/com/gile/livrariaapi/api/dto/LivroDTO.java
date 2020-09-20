package br.com.gile.livrariaapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivroDTO {

    private Long id;
    @NotEmpty
    private String titulo;
    @NotEmpty
    private String autor;
    @NotEmpty
    private String isbn;
}
