package com.essay.zaloapp.domain.payload.request.SocialMedia.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNewCommentRequest {
    private Long postId;

    private Long userId;

    private Long topComment;

    private String content;

    private MultipartFile file;
}
