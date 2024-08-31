package com.backend.lms.jwt;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class LoginResponse {

    private String jwtToken;
    private String message;

}
