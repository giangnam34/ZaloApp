package com.essay.zaloapp.controller;

import com.essay.zaloapp.Constant.Message;
import com.essay.zaloapp.domain.payload.request.ChangePasswordRequest;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.AuthenticationService;
import com.essay.zaloapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id, UserPrincipal principal){
        if (!(principal.getId() == id)) return ResponseEntity.badRequest().body(Message.WRONG_USERID);
        return userService.getUserById(id);
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal UserPrincipal userPrincipal){
        System.out.println("Id: " + userPrincipal.getId());
        System.out.println("Password: " + userPrincipal.getPassword());
        System.out.println("Phone number: " + userPrincipal.getPhoneNumber());
        return authenticationService.changePassword(changePasswordRequest,userPrincipal);
    }

    // Đổi ảnh đại diện
    @PostMapping("/changeImageAvatar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeImageAvatar(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        return userService.updateImageAvatar(userPrincipal.getId(),file);
    }

    // Đổi ảnh bìa
    @PostMapping("/changeImageCoverAvatar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeImageCoverAvatar(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        return userService.updateImageCoverAvatar(userPrincipal.getId(),file);
    }

    //Nhận ảnh đại diện
    @GetMapping(value = "/imageAvatar", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> getImageAvatar(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return userService.getImageAvatar(userPrincipal.getId());
    }

    // Nhận ảnh bìa
    @GetMapping(value = "/imageCoverAvatar", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> getImageCoverAvatar(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return userService.getImageCoverAvatar(userPrincipal.getId());
    }
}
