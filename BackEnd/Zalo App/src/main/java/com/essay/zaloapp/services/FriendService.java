package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.request.Friend.FriendRequest;
import org.springframework.http.ResponseEntity;

public interface FriendService {
    boolean isFriendUser(Long userId1, Long userId2);

    ResponseEntity<?> sendInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> acceptingInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> cancelInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> unFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> blockFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> listAllFriend(Long userId) throws Exception;

    ResponseEntity<?> listAllInviteFriend(Long userId) throws Exception;
}
