package br.com.gile.livrariaapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
}
