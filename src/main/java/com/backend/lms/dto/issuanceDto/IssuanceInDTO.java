package com.backend.lms.dto.issuanceDto;


import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class    IssuanceInDTO {

    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime issuedAt;
    private LocalDateTime returnedAt;
    private String status;
    private String issuanceType;

}
