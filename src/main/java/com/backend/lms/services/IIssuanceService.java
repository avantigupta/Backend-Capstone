package com.backend.lms.services;

import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO; // Use IssuanceOutDTO for consistency

import java.util.List;

public interface IIssuanceService {


    List<IssuanceOutDTO> getAllIssuances();

    IssuanceOutDTO getIssuanceById(Long id);

    String createIssuance(IssuanceInDTO issuanceDTO);

    String updateIssuance(Long id, IssuanceInDTO issuanceDTO);

    String deleteIssuance(Long id);

}