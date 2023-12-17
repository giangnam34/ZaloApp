package com.essay.zaloapp.domain.payload.request.SocialMedia.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAudiencePostRequest {

    private Long postId;

    private String audience;
}
