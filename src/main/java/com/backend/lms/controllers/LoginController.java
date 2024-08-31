//package com.backend.lms.controllers;
//
//
//import com.backend.lms.dto.loginDto.LoginDTO;
//import com.backend.lms.entities.Users;
//import com.backend.lms.jwt.JwtUtils;
//import com.backend.lms.jwt.LoginResponse;
//import com.backend.lms.services.Impl.UsersServiceImp;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//@RestController
//@RequestMapping(value = "api/users")
//public class LoginController {
//
//
//    @Autowired
//    UsersServiceImp usersServiceImp;
//    @Autowired
//    private JwtUtils jwtUtils;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//
//    @CrossOrigin
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
//
//        Authentication authentication;
//
//        try {
//
//            System.out.println("In Controller");
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
//
//                throw new Exception("Invalid login input format.");
//
//            }
//
//
//        } catch (Exception e) {
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("Messsage", "Bad credentials");
//            map.put("status", false);
//            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
//
//
//        }
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        String username = userDetails.getUsername();
//
//        Users user = usersServiceImp.getByUserName(username);
//
//        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
//
//        System.out.println(jwtToken);
//        LoginResponse response = new LoginResponse(jwtToken, userDetails.getUsername(), "ROLE_" + user.getRole(), user.getId());
//
//        return ResponseEntity.ok(response);
//
//    }
//
//
//    private boolean isEmail(String input) {
//        return input != null && input.contains("@");
//    }
//
//    private boolean isPhoneNumber(String input) {
//        return input != null && input.matches("\\d+");
//    }
//
//}

package com.backend.lms.controllers;


import com.backend.lms.dto.ResponseDto.ResponseDTO;
import com.backend.lms.dto.loginDto.LoginDTO;
import com.backend.lms.jwt.LoginResponse;
import com.backend.lms.services.ILoginService;
import com.backend.lms.services.Impl.LoginServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/users")
public class LoginController {

    @Autowired
    private LoginServiceImp iloginService;


    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginDTO loginDTO) throws Exception {

        LoginResponse response = iloginService.authenticateUser(loginDTO);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}