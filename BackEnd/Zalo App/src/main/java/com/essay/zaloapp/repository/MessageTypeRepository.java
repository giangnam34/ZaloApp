package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageTypeRepository extends JpaRepository<MessageType, Integer> {
}