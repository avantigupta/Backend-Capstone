package com.backend.lms.controllers;

import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.jwt.JwtUtils;
import com.backend.lms.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/users")
public class UsersController {

    @Autowired
    private IUsersService iUsersService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UsersInDTO usersInDTO) {
        System.out.println("Create");
        String response = iUsersService.createUser(usersInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UsersOutDTO>> getAllUsers() {
        System.out.println("I");
        List<UsersOutDTO> usersList = iUsersService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersOutDTO> getUserById(@PathVariable Long id) {
        UsersOutDTO user = iUsersService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersOutDTO> updateUser(@PathVariable Long id, @RequestBody UsersInDTO usersInDTO) {
        UsersOutDTO updatedUser = iUsersService.updateUser(id, usersInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = iUsersService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
