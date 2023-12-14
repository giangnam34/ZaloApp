package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.models.Post;
import com.essay.zaloapp.domain.models.PostUser;
import com.essay.zaloapp.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostUserRepository extends JpaRepository<PostUser, Long> {

    Boolean existsByPostAndUserAndPostUserType(Post post, User user, PostUserType postUserType);
    PostUser findFirstByPostAndUserAndPostUserTypeOrderByCreatedAtDesc(Post post, User user, PostUserType postUserType);

}