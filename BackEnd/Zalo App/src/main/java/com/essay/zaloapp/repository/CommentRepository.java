package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}