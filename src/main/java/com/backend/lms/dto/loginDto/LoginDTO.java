package com.backend.lms.dto.loginDto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String usernameOrPhoneNumber;
    private String password;

}
