package com.backend.lms.services.Impl;

import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Users;
import com.backend.lms.mapper.UsersMapper;
import com.backend.lms.repositories.UsersRepository;
import com.backend.lms.services.IUsersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersServiceImp implements IUsersService, UserDetailsService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public String createUser(UsersInDTO usersInDTO) {
        Users user = UsersMapper.mapToUsers(usersInDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser = usersRepository.save(user);

        return "User added successfully with ID: " + savedUser.getId();
    }

    @Override
    public UsersOutDTO getUserById(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        return UsersMapper.mapToUsersOutDTO(user);
    }

    @Override
    public List<UsersOutDTO> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();
        List<UsersOutDTO> usersOutDTOList = new ArrayList<>();

        usersList.forEach(user -> {
            usersOutDTOList.add(UsersMapper.mapToUsersOutDTO(user));
        });

        return usersOutDTOList;
    }

    @Override
    public UsersOutDTO updateUser(Long id, UsersInDTO usersInDTO) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        existingUser.setName(usersInDTO.getName());
        existingUser.setEmail(usersInDTO.getEmail());
        existingUser.setPhoneNumber(usersInDTO.getPhoneNumber());
        existingUser.setRole(usersInDTO.getRole());
        existingUser.setPassword(encoder.encode(usersInDTO.getPassword()));

        Users updatedUser = usersRepository.save(existingUser);
        return UsersMapper.mapToUsersOutDTO(updatedUser);
    }

    @Override
    public String deleteUser(Long id) {
        usersRepository.deleteById(id);
        return "User deleted successfully with ID: " + id;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhoneNumber) throws UsernameNotFoundException {
        Optional<Users> userOptional;

        if (usernameOrPhoneNumber.contains("@")) {
            userOptional = usersRepository.findByEmail(usernameOrPhoneNumber);
        } else {
            userOptional = usersRepository.findByPhoneNumber(usernameOrPhoneNumber);
        }

        Users userInfo = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with identifier: " + usernameOrPhoneNumber));

        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + userInfo.getRole()));

        return new User(userInfo.getEmail(), userInfo.getPassword(), grantedAuthorities);
    }

    @Override
    public Users getByUserName(String name) {
        Users user;
        if (name.contains("@")) {
            user = usersRepository.findByEmail(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name));
        } else {
            user = usersRepository.findByPhoneNumber(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name));
        }

        return user;
    }
}
