package br.com.controleestoque.repository;

import br.com.controleestoque.model.entity.ProdutoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoEstoqueRepository extends JpaRepository<ProdutoEstoque, UUID> {
}
