package com.backend.lms.dto.usersDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersOutDTO {

    private  Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
}
