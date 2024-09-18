package com.backend.lms.services.Impl;

import com.backend.lms.dto.loginDto.LoginDTO;
import com.backend.lms.entities.Users;
import com.backend.lms.jwt.JwtUtils;
import com.backend.lms.jwt.LoginResponse;
import com.backend.lms.services.ILoginService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.backend.lms.constants.constants.LOGIN_MESSAGE;

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
            if (isEmail(loginDTO.getUsernameOrPhoneNumber())) {
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsernameOrPhoneNumber(),
                                loginDTO.getPassword()
                        ));
            } else if (isPhoneNumber(loginDTO.getUsernameOrPhoneNumber())) {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsernameOrPhoneNumber(),
                                loginDTO.getPassword()
                        )
                );
            } else {
                throw new BadCredentialsException("Invalid login input format.");
            }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersServiceImp.getByUserName(username);
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        return new LoginResponse(jwtToken, LOGIN_MESSAGE, user.getId(), user.getName());
    }

    private boolean isEmail(String input) {
        return input != null && input.contains("@");
    }

    private boolean isPhoneNumber(String input) {
        return input != null && input.matches("\\d+");
    }
}
