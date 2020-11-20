package br.com.gile.livrariaapi.api.dto;

import lombok.Data;

@Data
public class EmprestimoFilterDTO {
    private String isbn;
    private String nome;
}
