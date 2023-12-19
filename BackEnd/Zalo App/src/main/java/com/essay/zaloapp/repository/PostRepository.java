package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByIdAndUser_Id(Long postId, Long userId);

    List<Post> findByUser_Id(Long userId);

}