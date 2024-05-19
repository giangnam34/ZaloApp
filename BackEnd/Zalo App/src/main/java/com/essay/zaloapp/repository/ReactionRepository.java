package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.PostUser;
import com.essay.zaloapp.domain.models.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
