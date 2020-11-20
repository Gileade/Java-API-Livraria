package br.com.gile.livrariaapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmprestimoFilterDTO {
    private String isbn;
    private String cliente;
}
