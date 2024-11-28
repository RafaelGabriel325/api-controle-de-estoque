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
    private static final Logger LOGGER = LoggerFactory.getLogger(TipoProdutoServiceImpl.class);
    private final TipoProdutoRepository tipoProdutoRepository;
    private static final TipoProdutoMapper mapper = TipoProdutoMapper.INSTANCE;

    @Override
    public TipoProdutoDTO findById(UUID id) {
        LOGGER.info("Finding Tipo Produto by ID: {}", id);
        TipoProduto tipoProdutoEntity = findTipoProdutoById(id);
        return addHateoasLinks(mapper.entityToDto(tipoProdutoEntity));
    }

    @Override
    public List<TipoProdutoDTO> findAll() {
        LOGGER.info("Finding all Tipo Produtos");
        return tipoProdutoRepository.findAll().stream()
                .map(mapper::entityToDto)
                .map(this::addHateoasLinks)
                .toList();
    }

    @Override
    public TipoProdutoDTO create(TipoProdutoDTO tipoProdutoDTO) {
        LOGGER.info("Creating a new Tipo Produto");
        TipoProduto tipoProdutoEntity = mapper.dtoToEntity(tipoProdutoDTO);
        tipoProdutoRepository.save(tipoProdutoEntity);
        return addHateoasLinks(mapper.entityToDto(tipoProdutoEntity));
    }

    @Override
    public void update(UUID id, TipoProdutoDTO tipoProdutoDTO) {
        LOGGER.info("Updating Tipo Produto with ID: {}", id);
        TipoProduto tipoProdutoEntity = findTipoProdutoById(id);
        tipoProdutoEntity.setNome(tipoProdutoDTO.getNome());
        tipoProdutoRepository.save(tipoProdutoEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting Tipo Produto with ID: {}", id);
        TipoProduto tipoProdutoEntity = findTipoProdutoById(id);
        tipoProdutoRepository.delete(tipoProdutoEntity);
    }

    private TipoProduto findTipoProdutoById(UUID id) {
        return tipoProdutoRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Tipo Produto not found with ID: {}", id);
                    return new TipoProdutoException("Tipo Produto not found with ID: " + id);
                });
    }

    private TipoProdutoDTO addHateoasLinks(TipoProdutoDTO tipoProdutoDTO) {
        tipoProdutoDTO.add(linkTo(methodOn(TipoProdutoController.class).findById(tipoProdutoDTO.getUuid())).withSelfRel());
        return tipoProdutoDTO;
    }
}