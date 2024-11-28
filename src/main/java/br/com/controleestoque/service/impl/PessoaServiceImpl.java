package br.com.controleestoque.service.impl;

import br.com.controleestoque.controller.PessoaController;
import br.com.controleestoque.exception.PessoaException;
import br.com.controleestoque.model.dto.PessoaDTO;
import br.com.controleestoque.model.entity.Pessoa;
import br.com.controleestoque.model.mapper.PessoaMapper;
import br.com.controleestoque.repository.PessoaRepository;
import br.com.controleestoque.service.PessoaService;
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
public class PessoaServiceImpl implements PessoaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PessoaServiceImpl.class);
    private final PessoaRepository pessoaRepository;
    private static final PessoaMapper mapper = PessoaMapper.INSTANCE;

    @Override
    public PessoaDTO findById(UUID id) {
        LOGGER.info("Finding Pessoa by ID: {}", id);
        Pessoa pessoaEntity = findPessoaById(id);
        return addHateoasLinks(mapper.entityToDto(pessoaEntity));
    }

    @Override
    public List<PessoaDTO> findAll() {
        LOGGER.info("Finding all Pessoas");
        return pessoaRepository.findAll().stream().map(mapper::entityToDto).map(this::addHateoasLinks).toList();
    }

    @Override
    public PessoaDTO create(PessoaDTO pessoaDTO) {
        LOGGER.info("Creating a new Pessoa");
        Pessoa pessoaEntity = mapper.dtoToEntity(pessoaDTO);
        pessoaRepository.save(pessoaEntity);
        return addHateoasLinks(mapper.entityToDto(pessoaEntity));
    }

    @Override
    public void update(UUID id, PessoaDTO pessoaDTO) {
        LOGGER.info("Updating Pessoa with ID: {}", id);
        Pessoa pessoaEntity = findPessoaById(id);
        pessoaEntity.setNome(pessoaDTO.getNome());
        pessoaEntity.setSobrenome(pessoaDTO.getSobrenome());
        pessoaRepository.save(pessoaEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting Pessoa with ID: {}", id);
        Pessoa pessoaEntity = findPessoaById(id);
        pessoaRepository.delete(pessoaEntity);
    }

    private Pessoa findPessoaById(UUID id) {
        return pessoaRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Pessoa not found with ID: {}", id);
            return new PessoaException("Pessoa not found with ID: " + id);
        });
    }

    private PessoaDTO addHateoasLinks(PessoaDTO pessoaDTO) {
        pessoaDTO.add(linkTo(methodOn(PessoaController.class).findById(pessoaDTO.getUuid())).withSelfRel());
        return pessoaDTO;
    }
}