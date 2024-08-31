package com.backend.lms.services.Impl;

import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import com.backend.lms.entities.Issuance;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.IssuanceMapper;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.repositories.UsersRepository;
import com.backend.lms.services.IIssuanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IssuanceServiceImp implements IIssuanceService {

    @Autowired
    private final IssuanceRepository issuanceRepository;

    @Autowired
    private final BooksRepository booksRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Override
    public List<IssuanceOutDTO> getAllIssuances() {
        List<Issuance> issuances = issuanceRepository.findAll();
        List<IssuanceOutDTO> issuanceDTOList = new ArrayList<>();

        issuances.forEach(issuance -> {
            IssuanceOutDTO issuanceOutDTO = new IssuanceOutDTO();
            issuanceDTOList.add(IssuanceMapper.mapToIssuanceDTO(issuance, issuanceOutDTO, booksRepository, usersRepository));
        });

        return issuanceDTOList;
    }

    @Override
    public IssuanceOutDTO getIssuanceById(Long id) {
        Issuance issuance = issuanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issuance not found with ID : " + id));

        IssuanceOutDTO issuanceOutDTO = new IssuanceOutDTO();
        return IssuanceMapper.mapToIssuanceDTO(issuance, issuanceOutDTO, booksRepository, usersRepository);
    }

    @Override
    public String createIssuance(IssuanceInDTO issuanceInDTO) {
        Issuance issuance = new Issuance();
        issuance = IssuanceMapper.mapToIssuance(issuanceInDTO, issuance, booksRepository, usersRepository);
        issuanceRepository.save(issuance);
        return "Issuance Added Successfully with ID : " + issuance.getId();
    }

    @Override
    public String updateIssuance(Long id, IssuanceInDTO issuanceInDTO) {
        Optional<Issuance> optionalIssuance = issuanceRepository.findById(id);

        if (optionalIssuance.isPresent()) {
            Issuance existingIssuance = optionalIssuance.get();
            existingIssuance = IssuanceMapper.mapToIssuance(issuanceInDTO, existingIssuance, booksRepository, usersRepository);
            issuanceRepository.save(existingIssuance);
            return "Issuance updated successfully with ID: " + id;
        } else {
            throw new ResourceNotFoundException("Issuance not found with ID : " + id);
        }
    }

    @Override
    public String deleteIssuance(Long id) {
        Issuance issuance = issuanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Issuance not found with ID: " + id));
        issuanceRepository.delete(issuance);

        return "Issuance deleted successfully with ID: " + id;
    }
}
