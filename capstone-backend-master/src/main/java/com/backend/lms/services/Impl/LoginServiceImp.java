package com.backend.lms.services.Impl;

import com.backend.lms.dto.loginDto.LoginDTO;
import com.backend.lms.entities.Users;
import com.backend.lms.jwt.JwtUtils;
import com.backend.lms.jwt.LoginResponse;
import com.backend.lms.services.ILoginService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.backend.lms.constants.constants.loginMessage;

@Service
@AllArgsConstructor
public class LoginServiceImp implements ILoginService {
    @Autowired
    private UsersServiceImp usersServiceImp;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse authenticateUser(LoginDTO loginDTO) throws Exception {

        Authentication authentication;

        try {

            if (isEmail(loginDTO.getUsernameOrPhoneNumber())) {
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrPhoneNumber(), loginDTO.getPassword()));
            } else if (isPhoneNumber(loginDTO.getUsernameOrPhoneNumber())) {

                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsernameOrPhoneNumber(),
                                loginDTO.getPassword()
                        )
                );
            } else {
                throw new Exception("Invalid login input format.");
            }

        } catch (Exception e) {

            Map<String, Object> map = new HashMap<>();
            map.put("Messsage", "Bad credentials");
            map.put("status", false);
            throw new Exception(e.getMessage());

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();


        Users user = usersServiceImp.getByUserName(username);

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        String message = loginMessage;
        return new LoginResponse(jwtToken, message);
    }

    private boolean isEmail(String input) {
        return input != null && input.contains("@");
    }

    private boolean isPhoneNumber(String input) {
        return input != null && input.matches("\\d+");
    }


}
