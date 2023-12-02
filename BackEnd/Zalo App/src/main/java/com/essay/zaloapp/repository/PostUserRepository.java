package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Composite.PostUserId;
import com.essay.zaloapp.domain.models.PostUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostUserRepository extends JpaRepository<PostUser, PostUserId> {
}