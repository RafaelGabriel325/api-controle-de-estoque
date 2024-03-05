package br.com.controleestoque.service.impl;

import br.com.controleestoque.controller.ProdutoEstoqueController;
import br.com.controleestoque.exception.PessoaException;
import br.com.controleestoque.exception.ProdutoEstoqueException;
import br.com.controleestoque.exception.TipoProdutoException;
import br.com.controleestoque.model.dto.ProdutoEstoqueDTO;
import br.com.controleestoque.model.entity.Pessoa;
import br.com.controleestoque.model.entity.ProdutoEstoque;
import br.com.controleestoque.model.entity.TipoProduto;
import br.com.controleestoque.model.mapper.ProdutoEstoqueMapper;
import br.com.controleestoque.repository.PessoaRepository;
import br.com.controleestoque.repository.ProdutoEstoqueRepository;
import br.com.controleestoque.repository.TipoProdutoRepository;
import br.com.controleestoque.service.ProdutoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class ProdutoEstoqueServiceImpl implements ProdutoEstoqueService {
    private final Logger LOGGER = LoggerFactory.getLogger(ProdutoEstoqueServiceImpl.class);
    private final ProdutoEstoqueRepository produtoEstoqueRepository;
    private final PessoaRepository pessoaRepository;
    private final TipoProdutoRepository tipoProdutoRepository;

    @Override
    public ProdutoEstoqueDTO findById(UUID id) {
        LOGGER.info("Finding an Produto by id");
        ProdutoEstoque produtoEstoqueEntity = this.produtoEstoqueRepository
                .findById(id)
                .orElseThrow(() -> new ProdutoEstoqueException("Produto not found with ID: " + id));
        ProdutoEstoqueDTO produtoEstoqueDTO = ProdutoEstoqueMapper.INSTANCE.entityToDto(produtoEstoqueEntity);
        produtoEstoqueDTO.add(linkTo(methodOn(ProdutoEstoqueController.class).findById(id)).withSelfRel());
        return produtoEstoqueDTO;
    }

    @Override
    public List<ProdutoEstoqueDTO> findAll() {
        LOGGER.info("Finding all Produto");
        List<ProdutoEstoqueDTO> prdutoDTOList = this.produtoEstoqueRepository.findAll().stream().map(ProdutoEstoqueMapper.INSTANCE::entityToDto).toList();
        prdutoDTOList.forEach(produto -> produto.add(linkTo(methodOn(ProdutoEstoqueController.class).findById(produto.getUuid())).withSelfRel()));
        return prdutoDTOList;
    }

    @Override
    public ProdutoEstoqueDTO create(ProdutoEstoqueDTO produtoEstoqueDTO) {
        LOGGER.info("Creating an Produto");
        ProdutoEstoque produtoEstoqueEntity = ProdutoEstoqueMapper.INSTANCE.dtoToEntity(produtoEstoqueDTO);
        Pessoa pessoaEntity = this.pessoaRepository
                .findById(produtoEstoqueDTO.getPessoa().getUuid())
                .orElseThrow(() -> new PessoaException("Pessoa not found with ID: " + produtoEstoqueDTO.getPessoa().getUuid()));
        TipoProduto tipoProdutoEntity = this.tipoProdutoRepository
                .findById(produtoEstoqueDTO.getTipoProduto().getUuid())
                .orElseThrow(() -> new TipoProdutoException("Pessoa not found with ID: " + produtoEstoqueDTO.getTipoProduto().getUuid()));
        produtoEstoqueEntity.setPessoa(pessoaEntity);
        produtoEstoqueEntity.setTipoProduto(tipoProdutoEntity);
        this.produtoEstoqueRepository.save(produtoEstoqueEntity);
        ProdutoEstoqueDTO createdProdutoEstoqueDTO = ProdutoEstoqueMapper.INSTANCE.entityToDto(produtoEstoqueEntity);
        createdProdutoEstoqueDTO.add(linkTo(methodOn(ProdutoEstoqueController.class).findById(createdProdutoEstoqueDTO.getUuid())).withSelfRel());
        return createdProdutoEstoqueDTO;
    }

    @Override
    public void update(UUID id, ProdutoEstoqueDTO produtoEstoqueDTO) {
        LOGGER.info("Updating a Produto");
        ProdutoEstoque produtoEstoqueEntity = this.produtoEstoqueRepository
                .findById(id)
                .orElseThrow(() -> new ProdutoEstoqueException("Email not found with ID: " + id));
        Pessoa pessoaEnity = this.pessoaRepository
                .findById(produtoEstoqueDTO.getPessoa().getUuid())
                .orElseThrow(() -> new PessoaException("Pessoa not found with ID: " + id));
        TipoProduto tipoProdutoEnity = this.tipoProdutoRepository
                .findById(produtoEstoqueDTO.getTipoProduto().getUuid())
                .orElseThrow(() -> new TipoProdutoException("Tipo Produto not found with ID: " + id));
        produtoEstoqueEntity.setTipoProduto(tipoProdutoEnity);
        produtoEstoqueEntity.setMarca(produtoEstoqueDTO.getMarca());
        produtoEstoqueEntity.setPessoa(pessoaEnity);
        produtoEstoqueEntity.setQuantidadePacote(produtoEstoqueDTO.getQuantidadePacote());
        produtoEstoqueEntity.setTamanhoPacote(produtoEstoqueDTO.getTamanhoPacote());
        produtoEstoqueEntity.setDataEntrega(produtoEstoqueDTO.getDataEntrega());
        produtoEstoqueRepository.save(produtoEstoqueEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting a Produto");
        ProdutoEstoque produtoEstoqueEntity = this.produtoEstoqueRepository
                .findById(id)
                .orElseThrow(() -> new ProdutoEstoqueException("Email not found with ID: " + id));
        this.produtoEstoqueRepository.delete(produtoEstoqueEntity);
    }
}
