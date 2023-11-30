package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.FriendRequest;
import com.essay.zaloapp.domain.payload.request.ChangeInfoUserRequest;
import com.essay.zaloapp.domain.payload.request.ChangePhoneNumberUserRequest;
import com.essay.zaloapp.domain.payload.request.FriendRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void swap(User user1, User user2);

    ResponseEntity<?> getUserById(Long id);

    ResponseEntity<?> updateImageAvatar(Long userId, MultipartFile file);

    Resource getImageAvatar(Long userId) throws Exception;

    Resource getImageAvatar(User user) throws Exception;

    Resource getImageAvatar(String phoneNumber) throws Exception;

    ResponseEntity<?> updateImageCoverAvatar(Long id, MultipartFile file);

    Resource getImageCoverAvatar(Long userId) throws Exception;

    Resource getImageCoverAvatar(User user) throws Exception;

    ResponseEntity<?> updatePhoneNumberUser(Long userId, ChangePhoneNumberUserRequest changePhoneNumberUserRequest) throws Exception;

    ResponseEntity<?> updateInfoUser(Long userId, ChangeInfoUserRequest changeInfoUserRequest) throws Exception;

    ResponseEntity<?> findUserByPhoneNumber(String phoneNumber) throws Exception;

    ResponseEntity<?> sendInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> acceptingInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> cancelInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> unFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> blockFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> listAllFriend(Long userId) throws Exception;

    ResponseEntity<?> listAllInviteFriend(Long userId) throws Exception;
}
