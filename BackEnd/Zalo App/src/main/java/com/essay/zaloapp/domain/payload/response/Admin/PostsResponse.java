package com.essay.zaloapp.domain.payload.response.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostsResponse {
    private Long id = null;
    private String content = null;
    private String audience = null;
    private String postUserPhoneNumber = null;
    private String postUserName = null;
    private String createdDate = null;
    private Long likesCount = null;
    private Long commentsCount = null;

}
