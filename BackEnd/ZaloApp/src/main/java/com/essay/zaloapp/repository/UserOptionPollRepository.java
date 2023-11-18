package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.UserOptionPoll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOptionPollRepository extends JpaRepository<UserOptionPoll, Integer> {
}