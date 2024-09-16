package com.backend.lms.controllers;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Users;
import com.backend.lms.jwt.JwtUtils;
import com.backend.lms.mapper.UsersMapper;
import com.backend.lms.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.backend.lms.constants.constants.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody List<UsersInDTO> usersInDTOList) {
        for (UsersInDTO usersInDTO : usersInDTOList) {
            usersService.createUser(usersInDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, USER_CREATE_MESSAGE));
    }

    @GetMapping
    public ResponseEntity<List<UsersOutDTO>> getAllUser() {
        List<UsersOutDTO> usersList = usersService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersOutDTO> getUserById(@PathVariable Long id) {
        UsersOutDTO user = usersService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @RequestBody UsersInDTO usersDTO) {
        usersService.updateUser(id, usersDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, USER_UPDATE_MESSAGE));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, USER_DELETE_MESSAGE));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        Long userCount = usersService.getUserCount();
        return ResponseEntity.status(HttpStatus.OK).body(userCount);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UsersOutDTO>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "search", required = false) String search
    ){
        Page<UsersOutDTO> users = usersService.getUsers(page, size, search);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/number/{number}")
    public  ResponseEntity<UsersOutDTO> getUserByMobile(@PathVariable String number) {

        UsersOutDTO usersOutDTO = usersService.getUserByMobile(number);

        return ResponseEntity.status(HttpStatus.OK).body(usersOutDTO);
    }

    @GetMapping("/user-by-phone")
    public ResponseEntity<UsersOutDTO> getUserByPhone(@RequestParam String phoneNumber) {
        Optional<Users> userOptional = usersService.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(UsersMapper.mapToUsersOutDTO(userOptional.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
