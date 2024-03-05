package br.com.controleestoque.service.impl;

import br.com.controleestoque.controller.TipoProdutoController;
import br.com.controleestoque.exception.TipoProdutoException;
import br.com.controleestoque.model.dto.TipoProdutoDTO;
import br.com.controleestoque.model.entity.TipoProduto;
import br.com.controleestoque.model.mapper.TipoProdutoMapper;
import br.com.controleestoque.repository.TipoProdutoRepository;
import br.com.controleestoque.service.TipoProdutoService;
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
public class TipoProdutoServiceImpl implements TipoProdutoService {
    private final Logger LOGGER = LoggerFactory.getLogger(TipoProdutoServiceImpl.class);
    private final TipoProdutoRepository tipoProdutoRepository;

    @Override
    public TipoProdutoDTO findById(UUID id) {
        LOGGER.info("Finding a Tipo Produto by id");
        TipoProduto tipoProdutoEntity = this.tipoProdutoRepository
                .findById(id)
                .orElseThrow(() -> new TipoProdutoException("Gender not found with ID: " + id));
        TipoProdutoDTO tipoProdutoDTO = TipoProdutoMapper.INSTANCE.entityToDto(tipoProdutoEntity);
        tipoProdutoDTO.add(linkTo(methodOn(TipoProdutoController.class).findById(id)).withSelfRel());
        return tipoProdutoDTO;
    }

    @Override
    public List<TipoProdutoDTO> findAll() {
        LOGGER.info("Finding all Tipo Produto");
        List<TipoProdutoDTO> tipoProdutoDTOList = this.tipoProdutoRepository.findAll().stream().map(TipoProdutoMapper.INSTANCE::entityToDto).toList();
        tipoProdutoDTOList.forEach(tipoProduto -> tipoProduto.add(linkTo(methodOn(TipoProdutoController.class).findById(tipoProduto.getUuid())).withSelfRel()));
        return tipoProdutoDTOList;
    }

    @Override
    public TipoProdutoDTO create(TipoProdutoDTO tipoProdutoDTO) {
        LOGGER.info("Creating a Tipo Produto");
        TipoProduto tipoProdutoEntity = TipoProdutoMapper.INSTANCE.dtoToEntity(tipoProdutoDTO);
        this.tipoProdutoRepository.save(tipoProdutoEntity);
        TipoProdutoDTO createdTipoProdutoDTO = TipoProdutoMapper.INSTANCE.entityToDto(tipoProdutoEntity);
        createdTipoProdutoDTO.add(linkTo(methodOn(TipoProdutoController.class).findById(createdTipoProdutoDTO.getUuid())).withSelfRel());
        return createdTipoProdutoDTO;
    }

    @Override
    public void update(UUID id, TipoProdutoDTO tipoProdutoDTO) {
        LOGGER.info("Updating a Tipo Produto");
        TipoProduto tipoProdutoEntity = this.tipoProdutoRepository
                .findById(id)
                .orElseThrow(() -> new TipoProdutoException("Tipo Produto not found with ID: " + id));
        tipoProdutoEntity.setNome(tipoProdutoDTO.getNome());
        tipoProdutoRepository.save(tipoProdutoEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting a Tipo Produto");
        TipoProduto tipoProdutoEntity = this.tipoProdutoRepository
                .findById(id)
                .orElseThrow(() -> new TipoProdutoException("Tipo Produto not found with ID: " + id));
        this.tipoProdutoRepository.delete(tipoProdutoEntity);
    }
}
