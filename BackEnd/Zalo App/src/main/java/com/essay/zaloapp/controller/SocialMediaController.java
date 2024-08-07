package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.payload.request.SocialMedia.Comment.AddNewCommentRequest;
import com.essay.zaloapp.domain.payload.request.SocialMedia.Comment.UpdateCommentRequest;
import com.essay.zaloapp.domain.payload.request.SocialMedia.CreateNewPostRequest;
import com.essay.zaloapp.domain.payload.request.SocialMedia.Post.UpdateAudiencePostRequest;
import com.essay.zaloapp.domain.payload.response.SocialMedia.Comment.InfoComment;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.SocialMediaService;
import com.essay.zaloapp.services.impl.SocialMediaServiceImpl;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/social-media/")
public class SocialMediaController {

    @Autowired()
    private SocialMediaService socialMediaService;

    // Đăng bài viết mới -- Checked
    @PostMapping(value = "/create-new-post", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute CreateNewPostRequest createNewPostRequest) {
        System.out.println(createNewPostRequest.toString());
        SocialMediaServiceImpl.GetInfoPost result = socialMediaService.createNewPost(userPrincipal.getId(), createNewPostRequest);
        return result.getMesage().equals("Đăng bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Cập nhật quyền riêng tư bài viết -- Checked
    @PostMapping(value = "/update-audience-post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateAudiencePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody(required = false) UpdateAudiencePostRequest updateAudiencePostRequest) {
        if (updateAudiencePostRequest == null || updateAudiencePostRequest.getPostId() == null || updateAudiencePostRequest.getAudience() == null || Audience.findByName(updateAudiencePostRequest.getAudience()) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Giá trị không hợp lệ!");
        String result = socialMediaService.updateAudiencePost(userPrincipal.getId(), updateAudiencePostRequest.getPostId(), Audience.findByName(updateAudiencePostRequest.getAudience()));
        return result.equals("Đổi đối tượng bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Cập nhật bài viết -- Checked
    @PostMapping(value = "/update-post/{postId}", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId, @ModelAttribute CreateNewPostRequest createNewPostRequest) {
        String result = socialMediaService.updatePost(userPrincipal.getId(), postId, createNewPostRequest);
        return result.equals("Chỉnh sửa bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/delete-post/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        String result = socialMediaService.deletePost(userPrincipal.getId(), postId);
        return result.equals("Xóa bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Xem tất cả bài viết đã đăng -- Checked
    @GetMapping(value = "/get-all-info-post", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllInfoPost(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        SocialMediaServiceImpl.GetAllInfoPostUser result = socialMediaService.getAllPostUser(userPrincipal.getId(), 0L);
        return result.getMesage().equals("Thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Thích bài viết -- Checked
    @PutMapping("/like-post/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> likePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        String result = socialMediaService.likePost(postId, userPrincipal.getId());
        return result.contains("thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Thêm bình luận mới -- Checked
    @PostMapping(value = "/create-new-comment", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute AddNewCommentRequest addNewCommentRequest) {
        System.out.println("Người dùng: " + userPrincipal.getId());
        System.out.println("AddNewCommentRequest: " + addNewCommentRequest);
        if (addNewCommentRequest.getPostId() == null)
            return ResponseEntity.badRequest().body("Post id không được phép null");
        if ((addNewCommentRequest.getContent() == null || addNewCommentRequest.getContent().isEmpty()) && (addNewCommentRequest.getFile() == null || addNewCommentRequest.getFile().isEmpty()))
            return ResponseEntity.badRequest().body("Nội dung bình luận không được phép rỗng!");
        String result = socialMediaService.createNewComment(addNewCommentRequest.getPostId(), userPrincipal.getId(), addNewCommentRequest.getTopComment(), addNewCommentRequest.getContent(), addNewCommentRequest.getFile());
        return result.equals("Đăng bình luận thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Cập nhật bình luận -- Checked
    @PutMapping(value = "/update-comment", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute UpdateCommentRequest updateCommentRequest) {
        if (updateCommentRequest.getCommentId() == null) {
            return ResponseEntity.badRequest().body("Comment id không được phép null");
        }

        String result;

        if (updateCommentRequest.getFile() instanceof MultipartFile) {
            // Handle MultipartFile case
            result = socialMediaService.updateComment(
                    userPrincipal.getId(),
                    updateCommentRequest.getCommentId(),
                    updateCommentRequest.getContent(),
                    (MultipartFile) updateCommentRequest.getFile()
            );
        } else if (updateCommentRequest.getFile() instanceof String) {
            // Handle String (URL) case
            result = socialMediaService.updateComment(
                    userPrincipal.getId(),
                    updateCommentRequest.getCommentId(),
                    updateCommentRequest.getContent(),
                    (String) updateCommentRequest.getFile()
            );
        } else if (updateCommentRequest.getFile() == null) {
            result = socialMediaService.updateComment(userPrincipal.getId(),
                    updateCommentRequest.getCommentId(),
                    updateCommentRequest.getContent(),
                    null);
        } else {
            // Handle unsupported case or return an error
            return ResponseEntity.badRequest().body("Unsupported file type");
        }


        return result.equals("Cập nhật bình luận thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Xóa bình luận -- Checked
    @DeleteMapping("/delete-comment/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId) {
        String result = socialMediaService.deleteComment(commentId, userPrincipal.getId());
        return result.equals("Xóa bình luận thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Thích bình luận -- Checked
    @PostMapping("/like-comment/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> likeComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId) {
        String result = socialMediaService.likeComment(userPrincipal.getId(), commentId);
        return result.contains("thành công") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // Số lượng bình luận -- Checked
    @GetMapping("/amount-comment/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> amountComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) throws Exception {
        Long result = socialMediaService.getAmountComment(postId, userPrincipal.getId());
        return ResponseEntity.ok(result);
    }

    // Thông tin tất cả bình luận -- checked
    @GetMapping("/get-all-info-comment/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllInfoComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) throws Exception {
        List<InfoComment> result = socialMediaService.getAllInfoComment(postId, userPrincipal.getId());
        System.out.println(result.size());
        return ResponseEntity.ok(result);
    }

    // Hiển thị bản tin
    @GetMapping("/get-post/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPostUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long page) throws IOException, TasteException {
        SocialMediaServiceImpl.GetAllInfoPostUser result = socialMediaService.getNewFeedUser(userPrincipal.getId(), page);
        return result.getMesage().equals("Thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/get-post-user/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPostOfUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long page) {
        SocialMediaServiceImpl.GetAllInfoPostUser result = socialMediaService.getAllPostUser(userPrincipal.getId(), page);
        return result.getMesage().equals("Thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/get-all-users-liked-post/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllUsersLikedPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        SocialMediaServiceImpl.GetAllUsersLikedPost result = socialMediaService.getAllUsersLikedPost(postId);
        return result.getMesage().equals("Lấy danh sách người thích thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/countPostsUsers")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> countPostUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long result = socialMediaService.countPostsUser(userPrincipal.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAPostInfo/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPostInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable Long postId){
        return ResponseEntity.ok(socialMediaService.getPostById(postId));
    }
}