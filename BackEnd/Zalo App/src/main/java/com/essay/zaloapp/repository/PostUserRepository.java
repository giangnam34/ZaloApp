package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.models.GroupChatUser;
import com.essay.zaloapp.domain.models.Post;
import com.essay.zaloapp.domain.models.PostUser;
import com.essay.zaloapp.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostUserRepository extends JpaRepository<PostUser, Long> {

    Boolean existsByPostAndUserAndPostUserType(Post post, User user, PostUserType postUserType);
    PostUser findFirstByPostAndUserAndPostUserTypeOrderByCreatedAtDesc(Post post, User user, PostUserType postUserType);

    @Query("SELECT pu FROM PostUser pu WHERE pu.post.id = :postId AND pu.user.id = :userId")
    List<PostUser> findAllByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("SELECT pu.user.id FROM PostUser pu WHERE pu.post.id = :postId")
    List<Long> findUserIdsByPostId(@Param("postId") Long postId);
}