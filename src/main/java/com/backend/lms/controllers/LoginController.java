package com.backend.lms.controllers;

import com.backend.lms.dto.loginDto.LoginDTO;
import com.backend.lms.jwt.LoginResponse;
import com.backend.lms.services.Impl.LoginServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping(value = "api/users")
public class LoginController {

    @Autowired
    private LoginServiceImp loginService;

    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginDTO loginDTO) throws Exception {

        String decodedPassword = new String(Base64.getDecoder().decode(loginDTO.getPassword()));
        loginDTO.setPassword(decodedPassword);
        LoginResponse response = loginService.authenticateUser(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

