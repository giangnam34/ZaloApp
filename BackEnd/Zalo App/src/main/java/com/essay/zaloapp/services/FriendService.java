package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.request.Friend.FriendRequest;
import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendService {
    boolean isFriendUser(Long userId1, Long userId2);

    ResponseEntity<?> sendInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> acceptingInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> cancelInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> unFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception;

    ResponseEntity<?> blockFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception;

    List<InfoUser> listAllFriend(Long userId) throws Exception;

    ResponseEntity<?> listAllInviteFriend(Long userId) throws Exception;

    ResponseEntity<?> listAllFriendAreNotInRoom(Long userId, Long roomId) throws Exception;

    ResponseEntity<?> listAllFriendIsBlockByUser(Long userId) throws Exception;

    ResponseEntity<?> unBlockFriendUser(Long userId, FriendRequest friendRequest) throws Exception;

    boolean isBlockUser(Long userId1, Long userId2);
}
