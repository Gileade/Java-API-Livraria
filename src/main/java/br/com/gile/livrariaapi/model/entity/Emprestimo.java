package br.com.gile.livrariaapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Emprestimo {

    private Long id;
    private String cliente;
    private Livro livro;
    private LocalDate dataDoEmprestimo;
    private Boolean retornado;
}
