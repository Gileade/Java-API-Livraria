package br.com.gile.livrariaapi.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Livro {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
}
