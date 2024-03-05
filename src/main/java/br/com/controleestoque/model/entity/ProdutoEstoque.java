package br.com.controleestoque.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "tab_produto_estoque")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProdutoEstoque implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID uuid;
    @Column(name = "marca", length = 25, nullable = false)
    private String marca;
    @Column(name = "quantidade_pacote", length = 2, nullable = false)
    private Integer quantidadePacote;
    @Column(name = "data_entrega", nullable = false)
    private LocalDate dataEntrega;
    @Column(name = "tamanho_pacote", length = 5, nullable = false)
    private String tamanhoPacote;
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "tipo_pessoa_id",
            referencedColumnName = "id", nullable = false)
    private TipoProduto tipoProduto;
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "pessoa_id",
            referencedColumnName = "id", nullable = false)
    private Pessoa pessoa;
}
