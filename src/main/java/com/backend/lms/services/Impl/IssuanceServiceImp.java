package com.backend.lms.services.Impl;
import com.backend.lms.dto.issuanceDto.IssuanceInDTO;
import com.backend.lms.dto.issuanceDto.IssuanceOutDTO;
import com.backend.lms.entities.Books;
import com.backend.lms.entities.Issuance;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.IssuanceMapper;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.repositories.UsersRepository;
import com.backend.lms.services.IIssuanceService;
import com.backend.lms.services.ISmsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class IssuanceServiceImp implements IIssuanceService {

    @Autowired
    private final IssuanceRepository issuanceRepository;

    @Autowired
    private final BooksRepository booksRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private ISmsService iSmsService;
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

    @Override
    public Page<IssuanceOutDTO> getIssuanceList(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "issuedAt"));
        Page<Issuance> issuances;
        if (search == null || search.isEmpty()) {
            issuances = issuanceRepository.findAll(pageRequest);
        } else {
            issuances = issuanceRepository.findByBookTitleOrUserName(search, pageRequest);
        }
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
        Books book = booksRepository.findById(issuanceDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + issuanceDTO.getBookId()));
        if (book.getQuantity() > 0) {

            book.setQuantity(book.getQuantity() - 1);
            booksRepository.save(book);
            Issuance issuance = IssuanceMapper.mapToIssuance(issuanceDTO, new Issuance(), booksRepository, usersRepository);
            issuance = issuanceRepository.save(issuance);
            Issuance savedIssuance =  issuanceRepository.save(issuance);
            Books booktoSend = booksRepository.findById(savedIssuance.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Book not found for ID: " + savedIssuance.getBook().getId()));

            String message = String.format("\nYou have issued the book '%s'\n" +
                                "author '%s'\n" +
                                "From %s\n" +
                                "To %s",

                        booktoSend.getTitle(),
                        booktoSend.getAuthor(),
                        savedIssuance.getIssuedAt().toLocalDate(),
                        savedIssuance.getReturnedAt().toLocalDate());

    //    iSmsService.sendSms(savedIssuance.getUser().getPhoneNumber(), message);
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

    @Override
    public String updateIssuance(Long id, IssuanceInDTO issuanceInDTO) {
        Optional<Issuance> optionalIssuance = issuanceRepository.findById(id);
        if (optionalIssuance.isPresent()) {
            Issuance existingIssuance = optionalIssuance.get();
            String originalStatus = existingIssuance.getStatus();

            if (issuanceInDTO.getStatus() != null) {
                existingIssuance.setStatus(issuanceInDTO.getStatus());
            }
            if ("Returned".equalsIgnoreCase(issuanceInDTO.getStatus()) &&
                    !"Returned".equalsIgnoreCase(originalStatus)) {
                Books book = booksRepository.findById(existingIssuance.getBook().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + existingIssuance.getBook().getId()));
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
        Long inHouseCount = issuanceRepository.countByIssuanceType("InHouse");
        Long externalCount = issuanceRepository.countByIssuanceType("Takeaway");
        Map<String, Long> countMap = new HashMap<>();
        countMap.put("InHouse", inHouseCount);
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
