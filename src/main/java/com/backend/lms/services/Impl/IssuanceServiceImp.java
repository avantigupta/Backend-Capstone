package com.backend.lms.services.Impl;

import com.backend.lms.dto.booksDto.BooksInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Category;
import com.backend.lms.entities.Issuance;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.IssuanceMapper;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.repositories.UsersRepository;
import com.backend.lms.services.IIssuanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            issuanceDTOList.add(IssuanceMapper.mapToIssuanceDTO(issuance, issuanceOutDTO));
        });

        return issuanceDTOList;
    }

//    @Override
//    public Page<IssuanceOutDTO> getIssuanceList(int page, int size, String search) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//
//        Page<Issuance> issuances = issuanceRepository.findAll(pageRequest);
//
//        return issuances.map(issuance -> IssuanceMapper.mapToIssuanceOutDTO(issuance, booksRepository, usersRepository));

    @Override
    public Page<IssuanceOutDTO> getIssuanceList(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "issuedAt"));

        // Fetch issuances based on search criteria
        Page<Issuance> issuances;
        if (search == null || search.isEmpty()) {
            issuances = issuanceRepository.findAll(pageRequest);
        } else {
            issuances = issuanceRepository.findByBookTitleOrUserName(search, pageRequest);
        }

        // Map issuances to IssuanceOutDTO
        return issuances.map(issuance -> IssuanceMapper.mapToIssuanceOutDTO(issuance, booksRepository, usersRepository));
    }


    @Override
    public IssuanceOutDTO getIssuanceById(Long id) {
        Issuance issuance = issuanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issuance not found with ID : " + id));

        IssuanceOutDTO issuanceOutDTO = new IssuanceOutDTO();
        return IssuanceMapper.mapToIssuanceDTO(issuance, issuanceOutDTO);
    }

    @Override
    public String createIssuance(IssuanceInDTO issuanceDTO) {
        Optional<Issuance> existingIssuance = issuanceRepository.findByUserIdAndBookId(issuanceDTO.getUserId(), issuanceDTO.getBookId());
        if (existingIssuance.isPresent()) {
            return "Issuance already exists for this user and book.";
        }

        // Fetch the book associated with the issuance
        Books book = booksRepository.findById(issuanceDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + issuanceDTO.getBookId()));

        // Check if there are enough copies available
        if (book.getQuantity() > 0) {
            // Decrease the book's quantity
            book.setQuantity(book.getQuantity() - 1);
            booksRepository.save(book);  // Save the updated book

            // Map the issuance DTO to the Issuance entity and save it
            Issuance issuance = IssuanceMapper.mapToIssuance(issuanceDTO, new Issuance(), booksRepository, usersRepository);
            issuance = issuanceRepository.save(issuance);

            return "Issuance Added Successfully with ID: " + issuance.getId();
        } else {
            return "No copies available for the selected book.";
        }
    }


    @Override
    public List<IssuanceOutDTO> getIssuanceByUserId(Long userId) {
        List<Issuance> issuances = issuanceRepository.findByUserId(userId);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        for (Issuance issuance : issuances) {
            IssuanceOutDTO issuanceOutDTO = new IssuanceOutDTO();
            IssuanceMapper.mapToIssuanceDTO(issuance, issuanceOutDTO);
            issuanceOutDTOList.add(issuanceOutDTO);
        }

        return issuanceOutDTOList;
    }

//    @Override
//    public String updateIssuance(Long id, IssuanceInDTO issuanceInDTO) {
//        Optional<Issuance> optionalIssuance = issuanceRepository.findById(id);
//        if (optionalIssuance.isPresent()) {
//            Issuance existingIssuance = optionalIssuance.get();
//
//            String originalStatus =  existingIssuance.getStatus();
//            if (issuanceInDTO.getStatus() != null) {
//                existingIssuance.setStatus(issuanceInDTO.getStatus());
//            }
//
//            if("Returned".equalsIgnoreCase(issuanceInDTO.getStatus()) &&
//                    !"Returned".equalsIgnoreCase(originalStatus)){
//
//                Optional<Books> bookopt = booksRepository.findById(existingIssuance.getBook().getId());
//
//                if(bookopt.isPresent()) {
//                    Books book = bookopt.get();
//                    book.setQuantity(book.getQuantity() +1 );
//                    booksRepository.save(book);
//                }else {
//                    throw  new ResourceNotFoundException("Book not found with ID :" + existingIssuance.getBook().getId());
//                }
//
//            }
//
//
//            issuanceRepository.save(existingIssuance);
//            return "Issuance updated successfully ";
//        } else {
//            throw new ResourceNotFoundException("Issuance not found with ID: " + id);
//        }
//    }

    @Override
    public String updateIssuance(Long id, IssuanceInDTO issuanceInDTO) {
        Optional<Issuance> optionalIssuance = issuanceRepository.findById(id);
        if (optionalIssuance.isPresent()) {
            Issuance existingIssuance = optionalIssuance.get();
            String originalStatus = existingIssuance.getStatus();

            if (issuanceInDTO.getStatus() != null) {
                existingIssuance.setStatus(issuanceInDTO.getStatus());
            }

            // If the issuance is marked as "Returned" and was previously "Issued"
            if ("Returned".equalsIgnoreCase(issuanceInDTO.getStatus()) &&
                    !"Returned".equalsIgnoreCase(originalStatus)) {
                Books book = booksRepository.findById(existingIssuance.getBook().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + existingIssuance.getBook().getId()));

                // Increase the book's quantity
                book.setQuantity(book.getQuantity() + 1);
                booksRepository.save(book);  // Save the updated book
            }

            issuanceRepository.save(existingIssuance);  // Save the updated issuance
            return "Issuance updated successfully.";
        } else {
            throw new ResourceNotFoundException("Issuance not found with ID: " + id);
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


    @Override
    public Map<String, Long> getCountByIssuanceType() {
        Long inHouseCount = issuanceRepository.countByIssuanceType("IN HOUSE");
        Long externalCount = issuanceRepository.countByIssuanceType("Takeaway");

        Map<String, Long> countMap = new HashMap<>();
        countMap.put("IN HOUSE", inHouseCount);
        countMap.put("Takeaway", externalCount);

        return countMap;
    }
    @Override
    public Long getIssuedCount() {
        Long count = issuanceRepository.countByStatus("Issued");
        System.out.println(count);
        return count;
    }

}
