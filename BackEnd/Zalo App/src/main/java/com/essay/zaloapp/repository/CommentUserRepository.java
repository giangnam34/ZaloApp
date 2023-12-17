package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Comment;
import com.essay.zaloapp.domain.models.CommentUser;
import com.essay.zaloapp.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentUserRepository extends JpaRepository<CommentUser, Long> {

    CommentUser findFirstByUserAndCommentOrderByCreatedAtDesc(User user, Comment comment);
}