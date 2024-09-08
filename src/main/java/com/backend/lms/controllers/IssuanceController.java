package com.backend.lms.controllers;

import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import com.backend.lms.services.IIssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.backend.lms.constants.constants.*;

@RestController
@RequestMapping(value = "api/v1/issuances")
public class IssuanceController {

    @Autowired
    private IIssuanceService iIssuanceService;

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<Page<IssuanceOutDTO>> getIssuanceList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "search", required = false) String search
    ) {

        Page<IssuanceOutDTO> issuanceOutDTO =iIssuanceService.getIssuanceList(page,size,search);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssuanceOutDTO> getIssuanceById(@PathVariable Long id) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.getIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @PostMapping("/save")
    public  ResponseEntity<String> createIssuance(@RequestBody IssuanceInDTO issuanceDTO) {
        String response = iIssuanceService.createIssuance(issuanceDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateIssuance(@PathVariable Long id, @RequestBody IssuanceInDTO issuanceInDTO) {
        String response = iIssuanceService.updateIssuance(id, issuanceInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteIssuance(@PathVariable Long id) {
        String response = iIssuanceService.deleteIssuance(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/userHistory")
    public String userHistory() {
        return "User History only accessible by User";
    }

    @GetMapping("/countByType")
    public ResponseEntity<Map<String, Long>> getCountByIssuanceType() {
        Map<String, Long> countMap = iIssuanceService.getCountByIssuanceType();
        return ResponseEntity.status(HttpStatus.OK).body(countMap);
    }


    @GetMapping("/countByStatus")
    public ResponseEntity<Long> getIssuedCount() {
        Long issuedCount = iIssuanceService.getIssuedCount();
        return ResponseEntity.status(HttpStatus.OK).body(issuedCount);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuanceByUserId(@PathVariable Long userId) {
        List<IssuanceOutDTO> issuances = iIssuanceService.getIssuanceByUserId(userId);
        return ResponseEntity.ok(issuances);
    }
}
