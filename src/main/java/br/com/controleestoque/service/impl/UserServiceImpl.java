package br.com.controleestoque.service.impl;

import br.com.controleestoque.controller.UserController;
import br.com.controleestoque.exception.UserException;
import br.com.controleestoque.model.dto.UserDTO;
import br.com.controleestoque.model.entity.User;
import br.com.controleestoque.model.mapper.UserMapper;
import br.com.controleestoque.repository.UserRepository;
import br.com.controleestoque.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Finding one User by name " + username + "!");
        UserDetails userDetails = userRepository.findByUsername(username);
        if (userDetails != null) {
            return userDetails;
        } else {
            throw new UsernameNotFoundException("Username" + username + " not found");
        }
    }

    @Override
    public UserDTO findById(UUID id) {
        LOGGER.info("Finding a User by id");
        User userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserException("User not found with ID: " + id));
        UserDTO userDTO = UserMapper.INSTANCE.entityToDto(userEntity);
        userDTO.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
        return userDTO;
    }

    @Override
    public List<UserDTO> findAll() {
        LOGGER.info("Finding all User");
        List<UserDTO> userDTOList = this.userRepository
                .findAll()
                .stream()
                .map(UserMapper.INSTANCE::entityToDto)
                .toList();
        userDTOList.forEach(user -> user.add(linkTo(methodOn(UserController.class).findById(user.getUuid())).withSelfRel()));
        return userDTOList;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        LOGGER.info("Creating a User");
        User userEntity = UserMapper.INSTANCE.dtoToEntity(userDTO);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        this.userRepository.save(userEntity);
        UserDTO createdUserDTO = UserMapper.INSTANCE.entityToDto(userEntity);
        createdUserDTO.add(linkTo(methodOn(UserController.class).findById(createdUserDTO.getUuid())).withSelfRel());
        return createdUserDTO;
    }

    @Override
    public void update(UUID id, UserDTO userDTO) {
        LOGGER.info("Updating a User");
        User userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserException("User not found with ID: " + id));
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setFullName(userEntity.getFullName());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting a User");
        User userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserException("User not found with ID: " + id));
        this.userRepository.delete(userEntity);
    }
}
