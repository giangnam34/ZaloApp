package com.essay.zaloapp.domain.payload.request.SocialMedia.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddNewCommentRequest {

    private Long postId;

    private Long topComment;

    private String content;

    private MultipartFile file;
}
