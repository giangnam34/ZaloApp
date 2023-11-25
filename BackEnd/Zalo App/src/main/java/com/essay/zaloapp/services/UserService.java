package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.request.ChangePhoneNumberUserRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<?> getUserById(Long id);

    ResponseEntity<?> updateImageAvatar(Long userId, MultipartFile file);

    ResponseEntity<Resource> getImageAvatar(Long userId) throws Exception;

    ResponseEntity<?> updateImageCoverAvatar(Long id, MultipartFile file);

    ResponseEntity<Resource> getImageCoverAvatar(Long userId) throws Exception;

    ResponseEntity<?> updatePhoneNumberUser(Long userId, ChangePhoneNumberUserRequest changePhoneNumberUserRequest) throws Exception;
}
