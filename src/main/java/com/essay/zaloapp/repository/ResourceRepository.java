package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}