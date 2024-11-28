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
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Finding user by username: {}", username);
        UserDetails userDetails = userRepository.findByUsername(username);
        if (userDetails != null) {
            return userDetails;
        } else {
            LOGGER.error("Username {} not found", username);
            throw new UsernameNotFoundException("Username" + username + " not found");
        }
    }

    @Override
    public UserDTO findById(UUID id) {
        LOGGER.info("Finding User by ID: {}", id);
        User userEntity = findUserById(id);
        return addHateoasLinks(mapper.entityToDto(userEntity));
    }

    @Override
    public List<UserDTO> findAll() {
        LOGGER.info("Finding all Users");
        return userRepository.findAll()
                .stream()
                .map(mapper::entityToDto)
                .map(this::addHateoasLinks)
                .toList();
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        LOGGER.info("Creating a new User");
        User userEntity = mapper.dtoToEntity(userDTO);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userEntity);
        return addHateoasLinks(mapper.entityToDto(userEntity));
    }

    @Override
    public void update(UUID id, UserDTO userDTO) {
        LOGGER.info("Updating User with ID: {}", id);
        User userEntity = findUserById(id);
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("Deleting User with ID: {}", id);
        User userEntity = findUserById(id);
        userRepository.delete(userEntity);
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("User not found with ID: {}", id);
                    return new UserException("User not found with ID: " + id);
                });
    }

    private UserDTO addHateoasLinks(UserDTO userDTO) {
        userDTO.add(linkTo(methodOn(UserController.class).findById(userDTO.getUuid())).withSelfRel());
        return userDTO;
    }
}