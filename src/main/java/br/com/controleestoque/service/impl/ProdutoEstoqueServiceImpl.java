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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoEstoqueServiceImpl.class);
    private final ProdutoEstoqueRepository produtoEstoqueRepository;
    private final PessoaRepository pessoaRepository;
    private final TipoProdutoRepository tipoProdutoRepository;
    private static final ProdutoEstoqueMapper mapper = ProdutoEstoqueMapper.INSTANCE;

    @Override
    public ProdutoEstoqueDTO findById(UUID id) {
        LOGGER.info("Finding ProdutoEstoque by ID: {}", id);
        ProdutoEstoque produtoEstoqueEntity = findProdutoEstoqueById(id);
        return addHateoasLinks(mapper.entityToDto(produtoEstoqueEntity));
    }

    @Override
    public List<ProdutoEstoqueDTO> findAll() {
        LOGGER.info("Finding all ProdutosEstoque");
        return produtoEstoqueRepository.findAll().stream()
                .map(mapper::entityToDto)
                .map(this::addHateoasLinks)
                .toList();
    }

    @Override
    public ProdutoEstoqueDTO create(ProdutoEstoqueDTO produtoEstoqueDTO) {
        LOGGER.info("Creating a new ProdutoEstoque");
        ProdutoEstoque produtoEstoqueEntity = mapper.dtoToEntity(produtoEstoqueDTO);
        Pessoa pessoaEntity = findPessoaById(produtoEstoqueDTO.getPessoa().getUuid());
        TipoProduto tipoProdutoEntity = findTipoProdutoById(produtoEstoqueDTO.getTipoProduto().getUuid());
        produtoEstoqueEntity.setPessoa(pessoaEntity);
        produtoEstoqueEntity.setTipoProduto(tipoProdutoEntity);
        produtoEstoqueRepository.save(produtoEstoqueEntity);
        return addHateoasLinks(mapper.entityToDto(produtoEstoqueEntity));
    }

    @Override
    public void update(UUID id, ProdutoEstoqueDTO produtoEstoqueDTO) {
        LOGGER.info("Updating ProdutoEstoque with ID: {}", id);
        ProdutoEstoque produtoEstoqueEntity = findProdutoEstoqueById(id);
        Pessoa pessoaEntity = findPessoaById(produtoEstoqueDTO.getPessoa().getUuid());
        TipoProduto tipoProdutoEntity = findTipoProdutoById(produtoEstoqueDTO.getTipoProduto().getUuid());
        produtoEstoqueEntity.setPessoa(pessoaEntity);
        produtoEstoqueEntity.setTipoProduto(tipoProdutoEntity);
        produtoEstoqueEntity.setMarca(produtoEstoqueDTO.getMarca());
        produtoEstoqueEntity.setQuantidadePacote(produtoEstoqueDTO.getQuantidadePacote());
        produtoEstoqueEntity.setTamanhoPacote(produtoEstoqueDTO.getTamanhoPacote());
        produtoEstoqueEntity.setDataEntrega(produtoEstoqueDTO.getDataEntrega());
        produtoEstoqueRepository.save(produtoEstoqueEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting ProdutoEstoque with ID: {}", id);
        ProdutoEstoque produtoEstoqueEntity = findProdutoEstoqueById(id);
        produtoEstoqueRepository.delete(produtoEstoqueEntity);
    }

    private ProdutoEstoque findProdutoEstoqueById(UUID id) {
        return produtoEstoqueRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("ProdutoEstoque not found with ID: {}", id);
                    return new ProdutoEstoqueException("Produto not found with ID: " + id);
                });
    }

    private Pessoa findPessoaById(UUID id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Pessoa not found with ID: {}", id);
                    return new PessoaException("Pessoa not found with ID: " + id);
                });
    }

    private TipoProduto findTipoProdutoById(UUID id) {
        return tipoProdutoRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("TipoProduto not found with ID: {}", id);
                    return new TipoProdutoException("TipoProduto not found with ID: " + id);
                });
    }

    private ProdutoEstoqueDTO addHateoasLinks(ProdutoEstoqueDTO produtoEstoqueDTO) {
        produtoEstoqueDTO.add(linkTo(methodOn(ProdutoEstoqueController.class).findById(produtoEstoqueDTO.getUuid())).withSelfRel());
        return produtoEstoqueDTO;
    }
}