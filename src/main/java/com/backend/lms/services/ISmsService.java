package com.backend.lms.services;

import org.springframework.stereotype.Service;

@Service
public interface ISmsService {
    void verifyNumber(String number);
    void sendSms(String toMobileNumber, String message);
}
