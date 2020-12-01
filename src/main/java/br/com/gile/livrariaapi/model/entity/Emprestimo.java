package br.com.gile.livrariaapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Emprestimo {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String cliente;

    @Column(name = "email_cliente")
    private String emailCliente;

    @JoinColumn(name = "id_livro")
    @ManyToOne
    private Livro livro;

    @Column
    private LocalDate dataDoEmprestimo;

    @Column
    private Boolean retornado;
}
