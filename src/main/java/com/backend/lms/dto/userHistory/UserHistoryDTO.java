package com.backend.lms.dto.userHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryDTO {

    private Long id;

    private String Book ;

    private String Category;

    private LocalDateTime issuedAt;

    private LocalDateTime returnedAt;

    private String status;

    private String issuanceType;
}