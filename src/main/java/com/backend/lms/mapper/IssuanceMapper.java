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

    public static IssuanceOutDTO mapToIssuanceDTO(Issuance issuance, IssuanceOutDTO issuanceOutDTO, BooksRepository booksRepository, UsersRepository usersRepository) {


        issuanceOutDTO.setIssuedAt(issuance.getIssuedAt());
        issuanceOutDTO.setReturnedAt(issuance.getReturnedAt());
        issuanceOutDTO.setStatus(issuance.getStatus());
        issuanceOutDTO.setIssuanceType(issuance.getIssuanceType());
        // Fetch Book details using bookId
        Books book = booksRepository.findById(issuance.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + issuance.getBookId()));
        issuanceOutDTO.setBook(book);

        // Fetch User details using userId
        Users user = usersRepository.findById(issuance.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + issuance.getUserId()));
        issuanceOutDTO.setUser(user);


        return issuanceOutDTO;


    }

    public static Issuance mapToIssuance(IssuanceInDTO issuanceInDTO, Issuance issuance, BooksRepository booksRepository, UsersRepository usersRepository) {

        issuance.setIssuedAt(issuanceInDTO.getIssuedAt());
        issuance.setReturnedAt(issuanceInDTO.getReturnedAt());
        issuance.setStatus(issuanceInDTO.getStatus());
        issuance.setIssuanceType(issuanceInDTO.getIssuanceType());

        Books book = booksRepository.findById(issuanceInDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + issuanceInDTO.getBookId()));
        issuance.setBookId(book.getId());

        Users user = usersRepository.findById(issuanceInDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + issuanceInDTO.getUserId()));
        issuance.setUserId(user.getId());

        return issuance;
    }


}
