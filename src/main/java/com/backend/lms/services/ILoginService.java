package com.backend.lms.services;

import com.backend.lms.dto.loginDto.LoginDTO;
import com.backend.lms.jwt.LoginResponse;

public interface ILoginService {

    LoginResponse authenticateUser(LoginDTO loginDTO) throws Exception;

}