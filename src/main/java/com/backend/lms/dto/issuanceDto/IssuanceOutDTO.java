package com.backend.lms.dto.issuanceDto;

import com.backend.lms.entities.Books;
import com.backend.lms.entities.Users;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IssuanceOutDTO {

    private Long id;
    private Users user;
    private Books book;
    private LocalDateTime issuedAt;
    private LocalDateTime returnedAt;
    private String status;
    private String issuanceType;

}
