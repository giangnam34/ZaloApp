package com.essay.zaloapp.domain.payload.request.SocialMedia.Post;

import com.essay.zaloapp.domain.enums.Audience;
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
