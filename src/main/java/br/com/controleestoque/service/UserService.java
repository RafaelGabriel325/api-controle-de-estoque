package br.com.controleestoque.service;

import br.com.controleestoque.model.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO findById(UUID id);

    List<UserDTO> findAll();

    UserDTO create(UserDTO userDTO);

    void update(UUID id, UserDTO userDTO);

    void delete(UUID id);
}
