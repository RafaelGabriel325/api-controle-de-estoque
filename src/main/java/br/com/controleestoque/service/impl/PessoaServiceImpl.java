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
    private final Logger LOGGER = LoggerFactory.getLogger(PessoaServiceImpl.class);
    private final PessoaRepository pessoaRepository;

    @Override
    public PessoaDTO findById(UUID id) {
        LOGGER.info("Finding a Pessoa by id");
        Pessoa pessoaEntity = this.pessoaRepository
                .findById(id)
                .orElseThrow(() -> new PessoaException("Pessoa not found with ID: " + id));
        PessoaDTO pessoaDTO = PessoaMapper.INSTANCE.entityToDto(pessoaEntity);
        pessoaDTO.add(linkTo(methodOn(PessoaController.class).findById(id)).withSelfRel());
        return pessoaDTO;
    }

    @Override
    public List<PessoaDTO> findAll() {
        LOGGER.info("Finding all Pessoa");
        List<PessoaDTO> pessoaDTOList = this.pessoaRepository
                .findAll()
                .stream()
                .map(PessoaMapper.INSTANCE::entityToDto)
                .toList();

        pessoaDTOList.forEach(pessoa -> pessoa.add(linkTo(methodOn(PessoaController.class).findById(pessoa.getUuid())).withSelfRel()));

        return pessoaDTOList;
    }

    @Override
    public PessoaDTO create(PessoaDTO pessoaDTO) {
        LOGGER.info("Creating a Pessoa");
        Pessoa pessoaEntity = PessoaMapper.INSTANCE.dtoToEntity(pessoaDTO);
        this.pessoaRepository.save(pessoaEntity);
        PessoaDTO createdPessoaDTO = PessoaMapper.INSTANCE.entityToDto(pessoaEntity);
        createdPessoaDTO.add(linkTo(methodOn(PessoaController.class).findById(createdPessoaDTO.getUuid())).withSelfRel());
        return createdPessoaDTO;
    }

    @Override
    public void update(UUID id, PessoaDTO pessoaDTO) {
        LOGGER.info("Updating a Pessoa");
        Pessoa pessoaEntity = this.pessoaRepository
                .findById(id)
                .orElseThrow(() -> new PessoaException("City not found with ID: " + id));
        pessoaEntity.setNome(pessoaDTO.getNome());
        pessoaEntity.setSobrenome(pessoaDTO.getSobrenome());


        pessoaRepository.save(pessoaEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting a Pessoa");
        Pessoa pessoaEntity = this.pessoaRepository
                .findById(id)
                .orElseThrow(() -> new PessoaException("City not found with ID: " + id));
        this.pessoaRepository.delete(pessoaEntity);
    }
}
