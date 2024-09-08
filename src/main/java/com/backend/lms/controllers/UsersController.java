package com.backend.lms.controllers;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Category;
import com.backend.lms.entities.Users;
import com.backend.lms.jwt.JwtUtils;
import com.backend.lms.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.lms.constants.constants.DELETE_MESSAGE;
import static com.backend.lms.constants.constants.OK_STATUS;

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
    public ResponseEntity<String> createUsers(@RequestBody List<UsersInDTO> usersInDTOList) {
        for (UsersInDTO usersInDTO : usersInDTOList) {
            iUsersService.createUser(usersInDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Users added successfully");
    }

    @GetMapping
    public ResponseEntity<List<UsersOutDTO>> getAllUsers() {
        List<UsersOutDTO> usersList = iUsersService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersOutDTO> getUserById(@PathVariable Long id) {
        UsersOutDTO user = iUsersService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UsersInDTO usersDTO) {
        String response =iUsersService.updateUser(id, usersDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Long id) {
        iUsersService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, DELETE_MESSAGE));
    }


    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        Long userCount = iUsersService.getUserCount();
        return ResponseEntity.status(HttpStatus.OK).body(userCount);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UsersOutDTO>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "search", required = false) String search
    ){
        Page<UsersOutDTO> users = iUsersService.getUsers(page, size, search);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    @CrossOrigin
    @GetMapping("/number/{number}")
    public  ResponseEntity<UsersOutDTO> getUserByMobile(@PathVariable String number) {

        UsersOutDTO usersOutDTO = iUsersService.getUserByMobile(number);

        return ResponseEntity.status(HttpStatus.OK).body(usersOutDTO);
    }




}
