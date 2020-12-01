package br.com.gile.livrariaapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Livro {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String titulo;
    @Column
    private String autor;
    @Column
    private String isbn;

    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY)
    private List<Emprestimo> emprestimos;
}
