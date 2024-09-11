package com.backend.lms.services.Impl;

import com.backend.lms.dto.usersDto.UsersInDTO;
import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Users;
import com.backend.lms.exception.MethodNotAllowedException;
import com.backend.lms.exception.ResourceConflictException;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.UsersMapper;
import com.backend.lms.provider.PasswordUtils;
import com.backend.lms.repositories.IssuanceRepository;
import com.backend.lms.repositories.UsersRepository;
import com.backend.lms.services.ISmsService;
import com.backend.lms.services.IUsersService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersServiceImp implements IUsersService, UserDetailsService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private IssuanceRepository issuanceRepository;

    private  final ISmsService iSmsService;

    @Override
    public String createUser(UsersInDTO usersInDTO) {
        String generatedPassword = passwordUtils.generateRandomPassword();
        String encodedPassword = passwordUtils.encodePassword(generatedPassword);
        Users user = UsersMapper.mapToUsers(usersInDTO);
        user.setPassword(encodedPassword);
        Users savedUser = usersRepository.save(user);
        String message = String.format( "\nWelcome %s\n" +
                        "Thankyou user, You have been successfully registered to BookNest! \n" +
                        "These are your login credentials\n" +
                        "Username: %s (OR) %s\n" +
                        "Password: %s",
                savedUser.getName(),
                savedUser.getPhoneNumber(),
                savedUser.getEmail(),
                generatedPassword);

              iSmsService.verifyNumber(savedUser.getPhoneNumber());
             iSmsService.sendSms(savedUser.getPhoneNumber(), message);
        return "User added successfully with ID: " + savedUser.getId() + ". The generated password is: " + generatedPassword;
    }

    @Override
    public Page<UsersOutDTO> getUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size , Sort.by(Sort.Direction.DESC, "id"));
        Page<Users> usersPage;
        if (search != null && !search.isEmpty()) {
            usersPage = usersRepository.findByNameContainingIgnoreCaseAndRoleEquals(search, "USER", pageable);
        } else {
            usersPage = usersRepository.findByRoleEquals("USER", pageable);
        }
        return usersPage.map(users -> UsersMapper.mapToUsersOutDTO(users));
    }

    @Override
    public UsersOutDTO getUserById(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return UsersMapper.mapToUsersOutDTO(user);
    }

    @Override
    public List<UsersOutDTO> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();
        List<UsersOutDTO> usersOutDTOList = new ArrayList<>();

        usersList.forEach(user -> {
            usersOutDTOList.add(UsersMapper.mapToUsersOutDTO(user));
        });
        return usersOutDTOList;
    }

    @Override
    public Long getUserCount() {
        return usersRepository.count();
    }

    @Override
    public String updateUser(Long id, UsersInDTO usersDTO) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        if (usersDTO.getPhoneNumber() != null) {
            Optional<Users> userWithSamePhoneNumber = usersRepository.findByPhoneNumber(usersDTO.getPhoneNumber());
            if (userWithSamePhoneNumber.isPresent() && !userWithSamePhoneNumber.get().getId().equals(id)) {
                throw new ResourceConflictException("Phone number already exists for another user.");
            }
            existingUser.setPhoneNumber(usersDTO.getPhoneNumber());
        }

        if (usersDTO.getEmail() != null) {
            if (!usersDTO.getEmail().endsWith(".com")) {
                throw new IllegalArgumentException("Email must end with '.com' domain.");
            }
            Optional<Users> userWithSameEmail = usersRepository.findByEmail(usersDTO.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                throw new ResourceConflictException("Email already exists for another user.");
            }
            existingUser.setEmail(usersDTO.getEmail());
        }
          if (usersDTO.getName() != null) {
            existingUser.setName(usersDTO.getName());
        }
       Users updatedUser = usersRepository.save(existingUser);
          UsersMapper.mapToUsersOutDTO(updatedUser);

        return "User updated successfully with ID: " + updatedUser.getId();
    }



    @Override
    public Optional<Users> findByPhoneNumber(String phoneNumber) {
        return usersRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    @Override
    public String deleteUser(Long id) {

        Optional<Users> user = usersRepository.findById(id);

        if (user.isEmpty()) {
            return  "User not Found";
        }
        boolean hasIssuedRecord = issuanceRepository.existsByUserIdAndStatus(id, "ISSUED");
            if (hasIssuedRecord) {
                throw new MethodNotAllowedException("User has issued books and cannot be deleted.");
            }
            issuanceRepository.deleteByUserId(id);
        usersRepository.deleteById(id);
        return  "User Deleted Successfully";
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhoneNumber) throws UsernameNotFoundException {
        Optional<Users> userOptional;
        if (usernameOrPhoneNumber.contains("@")) {
            userOptional = usersRepository.findByEmail(usernameOrPhoneNumber);
        } else {
            userOptional = usersRepository.findByPhoneNumber(usernameOrPhoneNumber);
        }
        if (userOptional.isPresent()) {
            System.out.println("INSIDE loadUserByUsername USER FOUND");
        } else {
            throw new UsernameNotFoundException("User not found with identifier: " + usernameOrPhoneNumber);
        }

        Users userInfo = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with identifier: " + usernameOrPhoneNumber));

        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + userInfo.getRole()));

        return new User(userInfo.getEmail(), userInfo.getPassword(), grantedAuthorities);
    }

    @Override
    public Users getByUserName(String name) {
        Users user;
        if (name.contains("@")) {
            user = usersRepository.findByEmail(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name));
        } else {
            user = usersRepository.findByPhoneNumber(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name));
        }
        return user;
    }

    @Override
    public UsersOutDTO getUserByMobile(String number) {
        Optional<Users> userOptional = usersRepository.findByPhoneNumber(number);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return UsersMapper.mapToUsersOutDTO(user);
        } else {
            throw new UsernameNotFoundException("User not found with phone number: " + number);
        }
    }
}
