package com.backend.lms.controllers;

import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import com.backend.lms.services.IIssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/issuances")
public class IssuanceController {

    @Autowired
    private IIssuanceService iIssuanceService;

    @GetMapping("/getAll")
    public ResponseEntity<List<IssuanceOutDTO>> getAllIssuances() {
        List<IssuanceOutDTO> issuanceDTOList = iIssuanceService.getAllIssuances();
        return ResponseEntity.status(HttpStatus.OK).body(issuanceDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssuanceOutDTO> getIssuanceById(@PathVariable Long id) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.getIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<String> createIssuance(@RequestBody IssuanceInDTO issuanceInDTO) {
        String response = iIssuanceService.createIssuance(issuanceInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIssuance(@PathVariable Long id, @RequestBody IssuanceInDTO issuanceInDTO) {
        String response = iIssuanceService.updateIssuance(id, issuanceInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIssuance(@PathVariable Long id) {
        String response = iIssuanceService.deleteIssuance(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/userHistory")
    public String userHistory() {
        return "User History only accessible by User";
    }
}
