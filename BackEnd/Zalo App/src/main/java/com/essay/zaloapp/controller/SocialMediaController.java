package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.payload.request.CreateNewPostRequest;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.SocialMediaService;
import com.essay.zaloapp.services.impl.SocialMediaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/social-media/")
public class SocialMediaController {

    @Autowired()
    private SocialMediaService socialMediaService;

    @PostMapping(value = "/create-new-post", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewPost(@AuthenticationPrincipal UserPrincipal userPrincipal,@ModelAttribute CreateNewPostRequest createNewPostRequest){
        String result = socialMediaService.createNewPost(userPrincipal.getId(),createNewPostRequest);
        return result.equals("Đăng bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @GetMapping(value = "/update-audience-post/{postId}/{audience}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateAudiencePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId, @PathVariable Audience audience){
        String result = socialMediaService.updateAudiencePost(userPrincipal.getId(), postId,audience);
        return result.equals("Đổi đối tượng bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PostMapping(value = "/update-audience-post/{postId}", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId, @ModelAttribute CreateNewPostRequest createNewPostRequest){
        String result = socialMediaService.updatePost(userPrincipal.getId(), postId,createNewPostRequest);
        return result.equals("Chỉnh sửa bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @GetMapping(value = "/get-all-info-post", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal){
        SocialMediaServiceImpl.GetAllInfoPost result = socialMediaService.getAllPostUser(userPrincipal.getId());
        return result.getMesage().equals("Thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

}
