package com.backend.lms.repositories;

import com.backend.lms.entities.Issuance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IssuanceRepository extends JpaRepository<Issuance, Long> {

}
