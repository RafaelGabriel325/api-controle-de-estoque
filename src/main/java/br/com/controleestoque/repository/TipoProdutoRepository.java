package br.com.controleestoque.repository;

import br.com.controleestoque.model.entity.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TipoProdutoRepository extends JpaRepository<TipoProduto, UUID> {
}
