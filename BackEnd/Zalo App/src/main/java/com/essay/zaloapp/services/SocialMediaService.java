package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.payload.request.SocialMedia.CreateNewPostRequest;
import com.essay.zaloapp.services.impl.SocialMediaServiceImpl;

public interface SocialMediaService {
    String createNewPost(Long userId, CreateNewPostRequest createNewPostRequest);

    String updateAudiencePost(Long userId, Long postId, Audience audience);

    String updatePost(Long userId, Long postId, CreateNewPostRequest createNewPostRequest);

    SocialMediaServiceImpl.GetAllInfoPost getAllPostUser(Long userId);
}
