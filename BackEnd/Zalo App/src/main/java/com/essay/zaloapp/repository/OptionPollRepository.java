package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.OptionPoll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionPollRepository extends JpaRepository<OptionPoll, Long> {
}