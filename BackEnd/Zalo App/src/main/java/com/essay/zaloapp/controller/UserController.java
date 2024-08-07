package com.essay.zaloapp.controller;

import com.essay.zaloapp.Constant.Message;
import com.essay.zaloapp.domain.payload.request.Authorize.ChangeInfoUserRequest;
import com.essay.zaloapp.domain.payload.request.Authorize.ChangePasswordRequest;
import com.essay.zaloapp.domain.payload.request.Authorize.ChangePhoneNumberUserRequest;
import com.essay.zaloapp.domain.payload.request.Friend.FriendRequest;
import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.AuthenticationService;
import com.essay.zaloapp.services.FriendService;
import com.essay.zaloapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private AuthenticationService authenticationService;

    // Tìm thông tin user
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id, UserPrincipal principal){
        if (!(principal.getId() == id)) {
            return ResponseEntity.badRequest().body(Message.WRONG_USERID);
        }
        return userService.getUserById(id);
    }

    // Đổi mật khẩu
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
    public ResponseEntity<?> changeImageAvatar(@RequestParam(value = "file", required = false) MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        return userService.updateImageAvatar(userPrincipal.getId(),file);
    }

    // Đổi ảnh bìa
    @PostMapping("/changeImageCoverAvatar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeImageCoverAvatar(@RequestParam(value = "file", required = false) MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        return userService.updateImageCoverAvatar(userPrincipal.getId(),file);
    }

    //Nhận ảnh đại diện
    @GetMapping(value = "/imageAvatar", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Resource> getImageAvatar(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return ResponseEntity.ok(userService.getImageAvatar(userPrincipal.getId()));
    }

    @GetMapping(value = "/imageAvatarAnotherUser/{phoneNumber}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getAnotherUserImageAvatar(@PathVariable String phoneNumber) throws Exception {
        return ResponseEntity.ok(userService.getImageAvatar(phoneNumber));
    }

    @GetMapping(value = "/imageCoverAvatarAnotherUser/{phoneNumber}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getAnotherUserImageCoverAvatar(@PathVariable String phoneNumber) throws Exception {
        return ResponseEntity.ok(userService.getImageCoverAvatar(phoneNumber));
    }

    // Nhận ảnh bìa
    @GetMapping(value = "/imageCoverAvatar", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Resource> getImageCoverAvatar(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return ResponseEntity.ok(userService.getImageCoverAvatar(userPrincipal.getId()));
    }

    // Đổi số điện thoại người dùng
    @PostMapping("/changePhoneNumber")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePhoneNumber(@RequestBody ChangePhoneNumberUserRequest changePhoneNumberUserRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return userService.updatePhoneNumberUser(userPrincipal.getId(),changePhoneNumberUserRequest);
    }

    // Cập nhật thông tin người dùng
    @PostMapping("/changeInfoUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeInfoUser(@RequestBody ChangeInfoUserRequest changeInfoUserRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        System.out.println(changeInfoUserRequest.toString());
        return userService.updateInfoUser(userPrincipal.getId(),changeInfoUserRequest);
    }

    // Tìm thông tin người dùng qua số điện thoại
    @GetMapping("/findUserByPhoneNumber/{phoneNumber}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findUserByPhoneNumber(@PathVariable String phoneNumber) throws Exception {
        return userService.findUserByPhoneNumber(phoneNumber);
    }

    @GetMapping("/findUserById/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findUserById(@PathVariable Long userId) throws Exception {
        return userService.findUserById(userId);
    }

    // Kết bạn với người dùng, friendRequest.fromPhoneNumberUser là người gửi lời mời, friendRequest.toPhoneNumberUser là người nhận lời mời
    @PostMapping("/sendInviteFriend")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addFriend(@RequestBody FriendRequest friendRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.sendInviteFriend(userPrincipal.getId(),friendRequest);
    }

    // Chấp nhận lời mời của người dùng, friendRequest.fromPhoneNumberUser là người chấp nhận lời mời, friendRequest.toPhoneNumberUser là người được chấp nhận lời mời (người gửi lời mời)
    @PostMapping("/acceptingInviteFriend")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> acceptingInviteFriend(@RequestBody FriendRequest friendRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.acceptingInviteFriend(userPrincipal.getId(),friendRequest);
    }

    // Hủy lời mời kết bạn
    @PostMapping("/cancelInviteFriend")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelInviteFriend(@RequestBody FriendRequest friendRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.cancelInviteFriend(userPrincipal.getId(),friendRequest);
    }

    // Hủy kết bạn
    @PostMapping("/unFriendUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unFriendUser(@RequestBody FriendRequest friendRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.unFriendUser(userPrincipal.getId(),friendRequest);
    }

    // Chặn người dùng
    @PostMapping("/blockFriendUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> blockFriendUser(@RequestBody FriendRequest friendRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.blockFriendUser(userPrincipal.getId(),friendRequest);
    }

    @PostMapping("/unBlockFriendUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unBlockFriendUser(@RequestBody FriendRequest friendRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.unBlockFriendUser(userPrincipal.getId(),friendRequest);
    }

    // Hiển thị danh sách bạn bè
    @GetMapping("/getAllFriendUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllFriendUser(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        List<InfoUser> listInfoUser = friendService.listAllFriend(userPrincipal.getId());
        if (listInfoUser != null){
            return ResponseEntity.ok(listInfoUser);
        }
        return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!");
    }

    @GetMapping("/getAllBlockedUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllBlockedUser(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.listAllFriendIsBlockByUser(userPrincipal.getId());
    }

    @GetMapping("/getListFriendsAreNotInRoom/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getListFriendsAreNotInRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long roomId) throws Exception {
        return friendService.listAllFriendAreNotInRoom(userPrincipal.getId(), roomId);
    }

    @GetMapping("/getAllInviteFriend")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllInviteFriendUser(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return friendService.listAllInviteFriend(userPrincipal.getId());
    }

    @GetMapping("/update-user-offline-activity/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserOfflineActivity(@PathVariable("id") Long id, UserPrincipal principal) throws Exception {
        if (!(principal.getId() == id)) {
            return ResponseEntity.badRequest().body(Message.WRONG_USERID);
        }
        if (userService.updateUserOfflineActivity(id)){
            return ResponseEntity.ok().body("");
        };
        return ResponseEntity.badRequest().body("");
    }
}
