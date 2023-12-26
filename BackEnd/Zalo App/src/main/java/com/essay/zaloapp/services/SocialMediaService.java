package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.models.Post;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.SocialMedia.CreateNewPostRequest;
import com.essay.zaloapp.domain.payload.response.SocialMedia.Comment.InfoComment;
import com.essay.zaloapp.domain.payload.response.SocialMedia.GetInfoPostResponse;
import com.essay.zaloapp.services.impl.SocialMediaServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocialMediaService {
    String createNewPost(Long userId, CreateNewPostRequest createNewPostRequest);

    String validateCreateNewPostRequest(User user, CreateNewPostRequest createNewPostRequest);

    String updateAudiencePost(Long userId, Long postId, Audience audience);

    String updatePost(Long userId, Long postId, CreateNewPostRequest createNewPostRequest);

    String deletePost(Long userId, Long postId);

    SocialMediaServiceImpl.GetAllInfoPostUser getAllPostUser(Long userId);

    SocialMediaServiceImpl.GetAllInfoPostUser getNewFeedUser(Long userId);

    List<GetInfoPostResponse> mapListPostEntityToResponse(List<Post> postList);

    GetInfoPostResponse mapPostEntityToResponse(Post p);

    String likePost(Long postId, Long userId);

    String createNewComment(Long postId, Long userId, Long topComment, String content, MultipartFile file);

    String updateComment(Long userId, Long commentId, String content, Object file);

    String deleteComment(Long commentId, Long userId);

    String likeComment(Long userId, Long commentId);

    Long getAmountComment(Long postId, Long userId) throws Exception;

    List<InfoComment> getAllInfoComment(Long postId, Long userId) throws Exception;

    InfoComment getInfoComment(Long commentId) throws Exception;
}