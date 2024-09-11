package com.backend.lms.services;

import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IIssuanceService {

    List<IssuanceOutDTO> getAllIssuances();

    List<IssuanceOutDTO> getIssuanceByUserId(Long userId);

    IssuanceOutDTO getIssuanceById(Long id);

    String createIssuance(IssuanceInDTO issuanceDTO);

    String updateIssuance(Long id, IssuanceInDTO issuanceDTO);

    String deleteIssuance(Long id);

    Map<String, Long> getCountByIssuanceType();

    Page<IssuanceOutDTO> getIssuanceList(int page, int size, String search);

    Long getIssuedCount();

}