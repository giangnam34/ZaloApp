package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Integer> {
}