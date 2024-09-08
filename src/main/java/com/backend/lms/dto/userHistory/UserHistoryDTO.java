package com.backend.lms.dto.userHistory;

import lombok.Data;

import java.time.LocalDateTime;



@Data
public class UserHistoryDTO {

    private Long id;

    private String Book ;

    private String Category;

    private LocalDateTime issuedAt;

    private LocalDateTime returnedAt;

    private String status;

    private String issuanceType;
}