package com.backend.lms.services.Impl;

import com.backend.lms.entities.Issuance;
import com.backend.lms.repositories.BooksRepository;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.repositories.UsersRepository;
import com.backend.lms.services.IBookReturnReminderService;
import com.backend.lms.services.ISmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReturnReminderServiceImp implements IBookReturnReminderService {

        private final BooksRepository booksRepository;
        private final IssuanceRepository issuanceRepository;
        private final UsersRepository usersRepository;
        private final ISmsService ismsService;

        @Override
        @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kolkata")
        public void sendReturnRemainder() {
            LocalDateTime startOfTomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
            LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusSeconds(1);
            List<Issuance> dueTomorrow = issuanceRepository.findAllByReturnedAtBetweenAndStatus(startOfTomorrow, endOfTomorrow, "ISSUED");
            System.out.println("SCHEDULER CALLED" + dueTomorrow);

            for (Issuance issuance : dueTomorrow) {
                String message = String.format("\nReminder:\n" +
                                "Please return the book '%s'\n" +
                                "Author '%s'\n"+
                                "by tomorrow (%s).",
                        issuance.getBook().getTitle(), issuance.getBook().getAuthor(),
                        issuance.getReturnedAt().toLocalDate());
                ismsService.sendSms(issuance.getUser().getPhoneNumber(), message);
                System.out.println(dueTomorrow);
            }
        }
}
