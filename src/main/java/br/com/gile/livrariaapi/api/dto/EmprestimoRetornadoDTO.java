package br.com.gile.livrariaapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmprestimoRetornadoDTO {
    private Boolean retornado;
}
