package com.essay.zaloapp.controller;

import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(adminService.getAllUsersForAdmin());
    }

    @GetMapping("/getAllPosts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPosts(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(adminService.getAllPostsForAdmin());
    }

    @GetMapping("/getAllGroups")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllGroups(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(adminService.getAllGroupsForAdmin());
    }

    @GetMapping("/getGeneralInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGeneralInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(adminService.getGeneralInfo());
    }

    @DeleteMapping("/deleteGroupChat/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteGroupChat(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long groupId) {
        String result = adminService.deleteGroupChatForAdmin(groupId);
        return result.equals("Xóa nhóm thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/deletePost/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        String result = adminService.deletePostForAdmin(postId);
        return result.equals("Xóa bài viết thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/lockUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> lockUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long userId) {
        String result = adminService.lockUserForAdmin(userId);
        return result.equals("Khóa người dùng thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }
}
