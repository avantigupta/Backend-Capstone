package com.backend.lms.controllers;


import com.backend.lms.services.IBooksService;
import com.backend.lms.services.ICategoryService;
import com.backend.lms.services.IIssuanceService;
import com.backend.lms.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    @Autowired
    private IBooksService booksService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IIssuanceService issuanceService;

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getAllCounts() {
        Map<String, Object> counts = new HashMap<>();

        Long bookCount = booksService.getBookCount();
        Long categoryCount = categoryService.getCategoryCount();
        Long userCount = usersService.getUserCount();
        Map<String, Long> issuanceCountByType = issuanceService.getCountByIssuanceType();
        Long activeUserCount = issuanceService.getIssuedCount();

        counts.put("bookCount", bookCount);
        counts.put("categoryCount", categoryCount);
        counts.put("userCount", userCount);
        counts.put("issuanceCountByType", issuanceCountByType);
        counts.put("activeUserCount", activeUserCount);

        return ResponseEntity.status(HttpStatus.OK).body(counts);
    }
}
