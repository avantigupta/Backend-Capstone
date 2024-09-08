package com.backend.lms.services;

import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Category;
import com.backend.lms.entities.Users;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IUsersService {

    String createUser(UsersInDTO usersInDTO);
    UsersOutDTO getUserById(Long id);
    List<UsersOutDTO> getAllUsers();
    String updateUser(Long id, UsersInDTO usersInDTO);
    Optional<Users> findByPhoneNumber(String phoneNumber);
    void deleteUser(Long id);
    Users getByUserName(String name);
    Long getUserCount();
    Page<UsersOutDTO> getUsers(int page, int size, String search);
    UsersOutDTO getUserByMobile(String number);
}
