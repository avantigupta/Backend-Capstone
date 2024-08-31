package com.backend.lms.mapper;

import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Users;

public class UsersMapper {

    private UsersMapper() {
    }

    public static UsersOutDTO mapToUsersOutDTO(Users user) {
        UsersOutDTO usersOutDTO = new UsersOutDTO();
        usersOutDTO.setName(user.getName());
        usersOutDTO.setEmail(user.getEmail());
        usersOutDTO.setPhoneNumber(user.getPhoneNumber());
        usersOutDTO.setRole(user.getRole());
        usersOutDTO.setPassword(user.getPassword());  // Be cautious about exposing passwords

        return usersOutDTO;
    }

    // Map UsersInDTO to Users entity
    public static Users mapToUsers(UsersInDTO usersInDTO) {
        Users user = new Users();
        user.setId(usersInDTO.getId());
        user.setName(usersInDTO.getName());
        user.setEmail(usersInDTO.getEmail());
        user.setPhoneNumber(usersInDTO.getPhoneNumber());
        user.setRole(usersInDTO.getRole());
        user.setPassword(usersInDTO.getPassword());

        return user;
    }

}
