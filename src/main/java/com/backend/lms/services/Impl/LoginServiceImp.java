//package com.backend.lms.services.Impl;
//
//import com.backend.lms.dto.loginDto.LoginDTO;
//import com.backend.lms.entities.Users;
//import com.backend.lms.jwt.JwtUtils;
//import com.backend.lms.jwt.LoginResponse;
//import com.backend.lms.services.ILoginService;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.backend.lms.constants.constants.LOGIN_MESSAGE;
//
//@Service
//@AllArgsConstructor
//public class LoginServiceImp implements ILoginService {
//    @Autowired
//    private UsersServiceImp usersServiceImp;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Override
//    public LoginResponse authenticateUser(LoginDTO loginDTO) throws Exception {
//
//        Authentication authentication;
//
//        try {
//
//            if (isEmail(loginDTO.getUsernameOrPhoneNumber())) {
//                authentication = authenticationManager
//                        .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrPhoneNumber(), loginDTO.getPassword()));
//            } else if (isPhoneNumber(loginDTO.getUsernameOrPhoneNumber())) {
//
//                authentication = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                loginDTO.getUsernameOrPhoneNumber(),
//                                loginDTO.getPassword()
//                        )
//                );
//            } else {
//                throw new Exception("Invalid login input format.");
//            }
//
//        } catch (Exception e) {
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("Messsage", "Bad credentials");
//            map.put("status", false);
//            throw new Exception(e.getMessage());
//
//        }
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        String username = userDetails.getUsername();
//
//
//        Users user = usersServiceImp.getByUserName(username);
//
//        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
//
//        return new LoginResponse(jwtToken, LOGIN_MESSAGE);
//    }
//
//    private boolean isEmail(String input) {
//        return input != null && input.contains("@");
//    }
//
//    private boolean isPhoneNumber(String input) {
//        return input != null && input.matches("\\d+");
//    }
//
//
//}

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

        try {
            // Check if the usernameOrPhoneNumber is an email or phone number
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
        } catch (BadCredentialsException e) {
            // Handle authentication errors
            throw new BadCredentialsException("Invalid username or password");
        } catch (Exception e) {
            // Handle other exceptions
            throw new Exception("An error occurred: " + e.getMessage());
        }

        // Set authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Fetch user details
        Users user = usersServiceImp.getByUserName(username);

        // Generate JWT token
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        System.out.println(user.getId());


        return new LoginResponse(jwtToken, LOGIN_MESSAGE, user.getId(), user.getName());
    }

    private boolean isEmail(String input) {
        return input != null && input.contains("@");
    }

    private boolean isPhoneNumber(String input) {
        return input != null && input.matches("\\d+");
    }
}
