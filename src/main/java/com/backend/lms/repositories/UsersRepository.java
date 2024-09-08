package com.backend.lms.repositories;

import com.backend.lms.dto.usersDto.UsersOutDTO;
import com.backend.lms.entities.Category;
import com.backend.lms.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    List<UsersOutDTO> findByRole(String role);

    Optional<Users> findByEmail(String email);
    Optional<Users> findByPhoneNumber(String phoneNumber);
    Page<Users> findByRoleEquals(String role, Pageable pageable);
    Page<Users> findByNameContainingIgnoreCaseAndRoleEquals(String name,String role, Pageable pageable);

}
