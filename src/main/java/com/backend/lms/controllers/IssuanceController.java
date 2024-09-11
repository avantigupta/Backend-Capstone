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

@CrossOrigin
@RestController
@RequestMapping(value = "api/issuances")
public class IssuanceController {

    @Autowired
    private IIssuanceService issuanceService;

    @GetMapping("/list")
    public ResponseEntity<Page<IssuanceOutDTO>> getIssuanceList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "search", required = false) String search
    ) {

        Page<IssuanceOutDTO> issuanceOutDTO = issuanceService.getIssuanceList(page,size,search);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssuanceOutDTO> getIssuanceById(@PathVariable Long id) {
        IssuanceOutDTO issuanceOutDTO = issuanceService.getIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @PostMapping("/save")
    public  ResponseEntity<ResponseDTO> createIssuance(@RequestBody IssuanceInDTO issuanceDTO) {
        issuanceService.createIssuance(issuanceDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, ISSUANCE_CREATE_MESSAGE));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateIssuance(@PathVariable Long id, @RequestBody IssuanceInDTO issuanceInDTO) {
        issuanceService.updateIssuance(id, issuanceInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, ISSUANCE_UPDATE_MESSAGE));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteIssuance(@PathVariable Long id) {
        issuanceService.deleteIssuance(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS, ISSUANCE_DELETE_MESSAGE));
    }

    @GetMapping("/userHistory")
    public String userHistory() {
        return "User History only accessible by User";
    }

    @GetMapping("/countByType")
    public ResponseEntity<Map<String, Long>> getCountByIssuanceType() {
        Map<String, Long> countMap = issuanceService.getCountByIssuanceType();
        return ResponseEntity.status(HttpStatus.OK).body(countMap);
    }


    @GetMapping("/countByStatus")
    public ResponseEntity<Long> getIssuedCount() {
        Long issuedCount = issuanceService.getIssuedCount();
        return ResponseEntity.status(HttpStatus.OK).body(issuedCount);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuanceByUserId(@PathVariable Long userId) {
        List<IssuanceOutDTO> issuances = issuanceService.getIssuanceByUserId(userId);
        return ResponseEntity.ok(issuances);
    }
}
