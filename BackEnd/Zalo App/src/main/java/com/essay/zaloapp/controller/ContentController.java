package com.essay.zaloapp.controller;

import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/media/")
public class ContentController {

    @Autowired
    private FileStorageService fileStorageService;
    @GetMapping(value = "/getImage/{name}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> getImage(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(fileStorageService.loadFileAsResource(name));
    }

    @GetMapping(value = "/getVideo/{name}", produces = "video/mp4")
    public ResponseEntity<Resource> getVideo(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(fileStorageService.loadFileAsResource(name));
    }

}
