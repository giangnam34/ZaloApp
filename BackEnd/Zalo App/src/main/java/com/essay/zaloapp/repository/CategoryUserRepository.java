package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.CategoryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryUserRepository extends JpaRepository<CategoryUser, Long> {
}