package com.essay.zaloapp.controller;

import com.essay.zaloapp.secruity.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/social-media/")
public class SocialMediaController {

    @PostMapping("/create-new-post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewPost(@RequestParam("files") MultipartFile[] multipartFileList){
        Arrays.stream(multipartFileList).forEach(file -> System.out.println(file.getOriginalFilename()));
        return null;
    }

}
