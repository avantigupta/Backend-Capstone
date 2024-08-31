package com.backend.lms.services;

import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Users;

import java.util.List;

public interface IUsersService {

    String createUser(UsersInDTO usersInDTO);

    UsersOutDTO getUserById(Long id);

    List<UsersOutDTO> getAllUsers();

    UsersOutDTO updateUser(Long id, UsersInDTO usersInDTO);

    String deleteUser(Long id);

    Users getByUserName(String name);
}
