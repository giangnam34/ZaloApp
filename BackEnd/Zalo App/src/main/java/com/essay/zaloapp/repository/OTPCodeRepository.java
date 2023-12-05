package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.OTPCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPCodeRepository extends JpaRepository<OTPCode, Long> {
}