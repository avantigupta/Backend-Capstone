package com.backend.lms.controllers;

import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import com.backend.lms.services.IIssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/userHistory")
public class UserHistoryController {

    @Autowired
    private IIssuanceService issuanceService;

    @GetMapping("/userIssuanceDetails/{userId}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuanceByUserId(@PathVariable Long userId) {

        List<IssuanceOutDTO> issuances = issuanceService.getIssuanceByUserId(userId);
        return ResponseEntity.ok(issuances);
    }
}
