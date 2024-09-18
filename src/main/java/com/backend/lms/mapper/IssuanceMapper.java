package com.backend.lms.mapper;

import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Issuance;
import com.backend.lms.entities.Users;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.UsersRepository;

public class IssuanceMapper {

    private IssuanceMapper() {
    }

    public static IssuanceOutDTO mapToIssuanceDTO(Issuance issuance, IssuanceOutDTO issuanceOutDTO) {

        issuanceOutDTO.setId(issuance.getId());
        issuanceOutDTO.setIssuedAt(issuance.getIssuedAt());
        issuanceOutDTO.setReturnedAt(issuance.getReturnedAt());
        issuanceOutDTO.setStatus(issuance.getStatus());
        issuanceOutDTO.setIssuanceType(issuance.getIssuanceType());
        issuanceOutDTO.setBook(issuance.getBook());
        issuanceOutDTO.setUser(issuance.getUser());
        return issuanceOutDTO;
    }

    public static Issuance mapToIssuance(IssuanceInDTO issuanceInDTO, Issuance issuance, BooksRepository booksRepository, UsersRepository usersRepository) {
        issuance.setId(issuanceInDTO.getId());
        issuance.setIssuedAt(issuanceInDTO.getIssuedAt());
        issuance.setReturnedAt(issuanceInDTO.getReturnedAt());
        issuance.setStatus(issuanceInDTO.getStatus());
        issuance.setIssuanceType(issuanceInDTO.getIssuanceType());
        Books book = booksRepository.findById(issuanceInDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + issuanceInDTO.getBookId()));
        issuance.setBook(book);
        Users user = usersRepository.findById(issuanceInDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + issuanceInDTO.getUserId()));
        issuance.setUser(user);

        return issuance;
    }
    public static IssuanceOutDTO mapToIssuanceOutDTO(Issuance issuance, BooksRepository bookRepository, UsersRepository userRepository) {
        IssuanceOutDTO issuanceOutDTO = new IssuanceOutDTO();

        issuanceOutDTO.setId(issuance.getId());
        issuanceOutDTO.setIssuedAt(issuance.getIssuedAt());
        issuanceOutDTO.setReturnedAt(issuance.getReturnedAt());
        issuanceOutDTO.setStatus(issuance.getStatus());
        issuanceOutDTO.setIssuanceType(issuance.getIssuanceType());
        Books book = bookRepository.findById(issuance.getBook().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + issuance.getBook().getId()));
        issuanceOutDTO.setBook(book);
        Users user = userRepository.findById(issuance.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + issuance.getUser().getId()));
        issuanceOutDTO.setUser(user);
        return issuanceOutDTO;
    }

}
