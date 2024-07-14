package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.FriendStatus;
import com.essay.zaloapp.domain.enums.ResourceType;
import com.essay.zaloapp.domain.enums.TypeNotification;
import com.essay.zaloapp.domain.models.*;
import com.essay.zaloapp.domain.models.Composite.FriendsId;
import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewRoomRequest;
import com.essay.zaloapp.domain.payload.request.Friend.FriendRequest;
import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatNotification;
import com.essay.zaloapp.domain.payload.response.Friend.GetAllInviteFriendResponse;
import com.essay.zaloapp.repository.*;
import com.essay.zaloapp.services.ChatMessageService;
import com.essay.zaloapp.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private MessageChatRepository messageChatRepository;

    @Autowired
    private GroupChatUserRepository groupChatUserRepository;

    @Autowired
    private ChatMessageService chatMessageService;

    // Kiểm tra 2 người có là bạn hay không
    @Override
    public boolean isFriendUser(Long userId1, Long userId2) {
        if (friendsRepository.existsFriendsByFriendsId(new FriendsId(Math.min(userId1, userId2), Math.max(userId1, userId2)))) {
            Friends friends = friendsRepository.findByFriendsId(new FriendsId(Math.min(userId1, userId2), Math.max(userId1, userId2)));
            if (friends.getFriendStatus() == FriendStatus.IsFriend && friends.getIsDelete() == 0 && friends.getIsBlock() == 0)
                return true;
            return false;
        }
        return false;
    }

    @Override
    public ResponseEntity<?> sendInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body(" Số điện thoại chưa đăng ký tài khoản hoặc không cho phép gửi lời mời kết bạn!");
            if (user1.getId() > user2.getId()) {
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            if (friendsRepository.existsFriendsByFriendsId(new FriendsId(user1.getId(), user2.getId()))) {
                Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(), user2.getId()));
                if (friends.getFriendStatus().equals(FriendStatus.WaitingAccept)) {
                    if (!friends.getSendInviteBy() && Objects.equals(user1.getId(), userId))
                        return ResponseEntity.badRequest().body("Đã gửi lời mời kết bạn từ trước. Vui lòng chờ người dùng phản hồi.");
                    else if (friends.getSendInviteBy() && Objects.equals(user2.getId(), userId))
                        return ResponseEntity.badRequest().body("Đã có lời mời kết bạn từ trước. Vui lòng chấp nhận hoặc hủy lời mời!");
                } else if (friends.getFriendStatus().equals(FriendStatus.IsFriend)) {
                    return ResponseEntity.badRequest().body("Hai người đã là bạn từ trước!");
                } else if (friends.getFriendStatus().equals(FriendStatus.ISBlock)) {
                    if ((friends.getIsBlock() == 1 && Objects.equals(user1.getId(), userId)) || (friends.getIsBlock() == 2 && Objects.equals(user2.getId(), userId)))
                        return ResponseEntity.badRequest().body("Bạn đã chặn người dùng này. Vui lòng bỏ chặn để gửi lời mời kết bạn!");
                    else
                        return ResponseEntity.badRequest().body("Người dùng đã chặn bạn! Xin thứ lỗi!");
                }
            }
            Friends friend = new Friends(new FriendsId(user1.getId(), user2.getId()), null, 0L, 0L, user2.getFullName(), user1.getFullName(), !Objects.equals(user1.getId(), userId), FriendStatus.WaitingAccept);
            friend.setUser1(user1);
            friend.setUser2(user2);
            friendsRepository.save(friend);
            return ResponseEntity.ok("Gửi lời mời kết bạn thành công!!!");
        } catch (Exception e) {
            System.out.println("message: " + e.getMessage());
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> acceptingInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()) {
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(), user2.getId()));
            System.out.println(friends.getSendInviteBy());
            if (!friends.getSendInviteBy() && Objects.equals(user1.getId(), userId) || friends.getSendInviteBy() && Objects.equals(user2.getId(), userId))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (friends.getFriendStatus().equals(FriendStatus.WaitingAccept)) {
                if (addFriendRequest.getIsAcceptingInvite()) {
                    friends.setFriendStatus(FriendStatus.IsFriend);
                    friends.setFriendFrom(new Date(new Date().getTime() + 7 * 60 * 60000));
                    friendsRepository.save(friends);

                    AddNewRoomRequest addNewRoomRequest = new AddNewRoomRequest();
                    List<Long> ids = new ArrayList<>();
                    ids.add(user1.getId());
                    addNewRoomRequest.setReceiverIds(ids);
                    chatMessageService.createRoom(userId, addNewRoomRequest);
                    return ResponseEntity.ok("Chấp nhận lời mời kết bạn thành công!!!");
                } else {
                    friendsRepository.delete(friends);
                    return ResponseEntity.ok("Không chấp nhận lời mời thành công!!!");
                }
            }
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!!!");
        } catch (Exception e) {
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> cancelInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()) {
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(), user2.getId()));
            if (friends == null || (friends.getSendInviteBy() && Objects.equals(user1.getId(), userId)) || (!friends.getSendInviteBy() && Objects.equals(user2.getId(), userId)))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (friends.getFriendStatus().equals(FriendStatus.WaitingAccept)) {
                friendsRepository.delete(friends);
                return ResponseEntity.ok("Hủy lời mời kết bạn thành công!!!");
            }
            return ResponseEntity.badRequest().body("Không thể hủy lời mời kết bạn!!!");
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> unFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()) {
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(), user2.getId()));
            if (friends.getFriendStatus().equals(FriendStatus.IsFriend)) {
                friends.setFriendStatus(FriendStatus.IsUnFriend);
                friends.setIsDelete(Objects.equals(userId, user1.getId()) ? 1L : 2L);
                friendsRepository.save(friends);
                return ResponseEntity.ok("Hủy kết bạn thành công!");
            } else {
                String message = "";
                if (friends.getFriendStatus().equals(FriendStatus.WaitingAccept))
                    message = "Bạn chưa kết bạn với người này! Hehe";
                else if (friends.getFriendStatus().equals(FriendStatus.ISBlock))
                    message = "Lêu lêu, người ta block m rồi! Ehe";
                else if (friends.getFriendStatus().equals(FriendStatus.IsUnFriend))
                    message = "Hai người từ lâu đã không đi chung đường!";
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            }
        } catch (Exception e) {
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> blockFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()) {
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            if (friendsRepository.existsFriendsByFriendsId(new FriendsId(user1.getId(), user2.getId()))) {
                Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(), user2.getId()));
                if (friends.getFriendStatus().equals(FriendStatus.ISBlock) && ((friends.getIsBlock() == 1 && Objects.equals(user2.getId(), userId)) || (friends.getIsBlock() == 2 && Objects.equals(user1.getId(), userId)))) {
                    return ResponseEntity.badRequest().body("Người dùng đã block bạn từ trước!!!");
                }
                friends.setFriendStatus(FriendStatus.ISBlock);
                friends.setIsBlock(Objects.equals(user1.getId(), userId) ? 1L : 2L);
                friendsRepository.save(friends);

                User user = userRepository.findById(userId);
                List<GroupChatUser> groupChatUserList1 = groupChatUserRepository.findByIdPhoneNumberUser(user.getPhoneNumber());
                List<GroupChatUser> groupChatUserList2 = groupChatUserRepository.findByIdPhoneNumberUser(user2.getPhoneNumber());

                List<Long> groupChatIds = new ArrayList<>();

                for (GroupChatUser groupChatUser1 : groupChatUserList1) {
                    for (GroupChatUser groupChatUser2 : groupChatUserList2) {
                        if (groupChatUser1.getId().getGroupId() == groupChatUser2.getId().getGroupId()) {
                            groupChatIds.add(groupChatUser1.getId().getGroupId());
                        }
                    }
                }

                Long groupChatId = null;

                for (Long id : groupChatIds) {
                    List<GroupChatUser> check = groupChatUserRepository.findAllByGroupId(id);
                    if (check.size() == 2) {
                        groupChatId = id;
                        break;
                    }
                }

                MessageChat messageChat = new MessageChat();
                GroupChat groupChat = groupChatRepository.findById(groupChatId).get();

                messageChat.setUser(user);
                messageChat.setSeen(true);
                messageChat.setGroupChat(groupChat);
                messageChat.setIsSystem(true);
                messageChat.setContent("Không thể tiếp tục gửi tin nhắn do đã bị chặn từ người dùng " + user.getFullName());
                messageChat.setSendAt(new Date(new Date().getTime()));
                messageChat.setUpdatedAt(new Date(new Date().getTime()));
                messageChatRepository.save(messageChat);
                return ResponseEntity.ok("Chặn người dùng này thành công!!!");
            }
            Friends friends = new Friends(new FriendsId(user1.getId(), user2.getId()), null, Objects.equals(user1.getId(), userId) ? 1L : 2L, 0L, user2.getFullName(), user1.getFullName(), null, FriendStatus.ISBlock);
            friendsRepository.save(friends);

            User user = userRepository.findById(userId);
            List<GroupChatUser> groupChatUserList1 = groupChatUserRepository.findByIdPhoneNumberUser(user.getPhoneNumber());
            List<GroupChatUser> groupChatUserList2 = groupChatUserRepository.findByIdPhoneNumberUser(user2.getPhoneNumber());

            List<Long> groupChatIds = new ArrayList<>();

            for (GroupChatUser groupChatUser1 : groupChatUserList1) {
                for (GroupChatUser groupChatUser2 : groupChatUserList2) {
                    if (groupChatUser1.getId().getGroupId() == groupChatUser2.getId().getGroupId()) {
                        groupChatIds.add(groupChatUser1.getId().getGroupId());
                    }
                }
            }

            Long groupChatId = null;

            for (Long id : groupChatIds) {
                List<GroupChatUser> check = groupChatUserRepository.findAllByGroupId(id);
                if (check.size() == 2) {
                    groupChatId = id;
                    break;
                }
            }

            MessageChat messageChat = new MessageChat();
            GroupChat groupChat = groupChatRepository.findById(groupChatId).get();

            messageChat.setUser(user);
            messageChat.setSeen(true);
            messageChat.setGroupChat(groupChat);
            messageChat.setIsSystem(true);
            messageChat.setContent("Không thể tiếp tục gửi tin nhắn do đã bị chặn từ người dùng " + user.getFullName());
            messageChat.setSendAt(new Date(new Date().getTime()));
            messageChat.setUpdatedAt(new Date(new Date().getTime()));
            MessageChat response = messageChatRepository.save(messageChat);

            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(groupChatId);
            for (GroupChatUser groupChatUser : groupChatUsers) {
                if (!groupChatUser.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                    chatMessageService.notifyToUser(userRepository.findByPhoneNumber(groupChatUser.getId().getPhoneNumberUser()).getId(), ChatNotification.builder().roomId(groupChatId).typeNotification(TypeNotification.UPDATE).message(response).build());
                }
            }

            return ResponseEntity.ok("Chặn người dùng này thành công!!!");
        } catch (Exception e) {
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public List<InfoUser> listAllFriend(Long userId) throws Exception {
        try {
            List<Friends> friendsList = friendsRepository.findByFriendsIdUser1(userId);
            friendsList.addAll(friendsRepository.findByFriendsIdUser2(userId));
            List<InfoUser> result = new ArrayList<>();
            for (Friends friend : friendsList) {
                if (friend.getFriendStatus().equals(FriendStatus.IsFriend)){
                    InfoUser infoUser = InfoUser.builder()
                            .userName(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getFullName() : friend.getUser1().getFullName())
                            .imageAvatar("http://localhost:8181/v1/users/imageAvatarAnotherUser/" + (Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber()))
                            .phoneNumber(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber())
                            .status(String.valueOf(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getStatus() : friend.getUser1().getStatus()))
                            .build();
                    result.add(infoUser);
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> listAllInviteFriend(Long userId) throws Exception {
        try {
            User user = userRepository.findById(userId);
            List<Friends> friendsList = friendsRepository.findByFriendsIdUser1(userId);
            friendsList.addAll(friendsRepository.findByFriendsIdUser2(userId));
            List<GetAllInviteFriendResponse> result = new ArrayList<>();
            for (Friends friend : friendsList) {
                if (friend.getFriendStatus().equals(FriendStatus.WaitingAccept))
                    result.add(new GetAllInviteFriendResponse(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getFullName() : friend.getUser1().getFullName(),
                            "http://localhost:8181/v1/users/imageAvatarAnotherUser/" + (Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber()),
                            Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber(),
                            (friend.getSendInviteBy() && Objects.equals(userId, friend.getUser2().getId())) ||
                                    (!friend.getSendInviteBy() && Objects.equals(userId, friend.getUser1().getId()))));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!");
        }
    }

    @Override
    public ResponseEntity<?> listAllFriendAreNotInRoom(Long userId, Long roomId) throws Exception {
        try {
            List<Friends> friendsList = friendsRepository.findByFriendsIdUser1(userId);
            friendsList.addAll(friendsRepository.findByFriendsIdUser2(userId));

            List<GroupChatUser> groupChatUserList = groupChatUserRepository.findAllByGroupId(roomId);

            Set<String> groupChatUserPhoneNumbers = groupChatUserList.stream()
                    .map(groupChatUser -> groupChatUser.getId().getPhoneNumberUser())
                    .collect(Collectors.toSet());

            List<InfoUser> result = new ArrayList<>();
            for (Friends friend : friendsList) {

                String friendPhoneNumber = Objects.equals(userId, friend.getUser1().getId())
                        ? friend.getUser2().getPhoneNumber()
                        : friend.getUser1().getPhoneNumber();

                if (!groupChatUserPhoneNumbers.contains(friendPhoneNumber) && friend.getFriendStatus().equals(FriendStatus.IsFriend)) {
                    InfoUser infoUser = InfoUser.builder()
                            .userName(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getFullName() : friend.getUser1().getFullName())
                            .imageAvatar("http://localhost:8181/v1/users/imageAvatarAnotherUser/" + friendPhoneNumber)
                            .phoneNumber(friendPhoneNumber)
                            .status(String.valueOf(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getStatus() : friend.getUser1().getStatus()))
                            .build();
                    result.add(infoUser);
                }
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!");
        }
    }

    @Override
    public ResponseEntity<?> listAllFriendIsBlockByUser(Long userId) throws Exception {
        try {
            List<Friends> friendsList = friendsRepository.findByFriendsIdUser1(userId);
            friendsList.addAll(friendsRepository.findByFriendsIdUser2(userId));
            List<InfoUser> result = new ArrayList<>();
            for (Friends friend : friendsList) {
                if (friend.getFriendStatus().equals(FriendStatus.ISBlock) &&
                        ((friend.getIsBlock() == 1 && Objects.equals(userId, friend.getUser1().getId())) ||
                                (friend.getIsBlock() == 2 && Objects.equals(userId, friend.getUser2().getId())))) {
                    result.add(new InfoUser(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getFullName() : friend.getUser1().getFullName(),
                            "http://localhost:8181/v1/users/imageAvatarAnotherUser/" + (Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber()),
                            Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber(),String.valueOf(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getStatus() : friend.getUser1().getStatus())));
                }
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!");
        }
    }

    @Override
    public ResponseEntity<?> unBlockFriendUser(Long userId, FriendRequest friendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(friendRequest.getFromPhoneNumberUser()) || friendRequest.getFromPhoneNumberUser().equals(friendRequest.getToPhoneNumberUser())) {
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            }
            if (friendRequest.getToPhoneNumberUser().length() < 10) {
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            }
            User user2 = userRepository.findByPhoneNumber(friendRequest.getToPhoneNumberUser());
            if (user2 == null) {
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            }
            if (user1.getId() > user2.getId()) {
                User temp = user1;
                user1 = user2;
                user2 = temp;
            }

            FriendsId friendsId = new FriendsId(user1.getId(), user2.getId());
            if (friendsRepository.existsFriendsByFriendsId(friendsId)) {
                Friends friends = friendsRepository.findByFriendsId(friendsId);
                if (friends.getFriendStatus().equals(FriendStatus.ISBlock) &&
                        ((friends.getIsBlock() == 1 && Objects.equals(user1.getId(), userId)) ||
                                (friends.getIsBlock() == 2 && Objects.equals(user2.getId(), userId)))) {
                    friends.setFriendStatus(FriendStatus.IsFriend);
                    friends.setIsBlock(0L);
                    friendsRepository.save(friends);

                    List<GroupChatUser> groupChatUserList1 = groupChatUserRepository.findByIdPhoneNumberUser(user1.getPhoneNumber());
                    List<GroupChatUser> groupChatUserList2 = groupChatUserRepository.findByIdPhoneNumberUser(user2.getPhoneNumber());

                    List<Long> groupChatIds = new ArrayList<>();

                    for (GroupChatUser groupChatUser1 : groupChatUserList1) {
                        for (GroupChatUser groupChatUser2 : groupChatUserList2) {
                            if (groupChatUser1.getId().getGroupId().equals(groupChatUser2.getId().getGroupId())) {
                                List<GroupChatUser> groupChatUsersForTest = groupChatUserRepository.findAllByGroupId(groupChatUser1.getId().getGroupId());
                                if (groupChatUsersForTest.size() == 2) {
                                    groupChatIds.add(groupChatUser1.getId().getGroupId());
                                }
                            }
                        }
                    }

                    for (Long groupId : groupChatIds) {
                        List<MessageChat> messages = messageChatRepository.findAllByGroupIdNoPagination(groupId);
                        for (MessageChat message : messages) {
                            if (message.getIsSystem() && message.getContent().contains("Không thể tiếp tục gửi tin nhắn do đã bị chặn từ người dùng")) {
                                message.setDeleted(true);
                                messageChatRepository.save(message);
                            }
                        }
                    }

                    return ResponseEntity.ok("Đã bỏ chặn người dùng này thành công!!!");
                } else {
                    return ResponseEntity.badRequest().body("Người dùng này không bị chặn bởi bạn!");
                }
            } else {
                return ResponseEntity.badRequest().body("Người dùng này chưa là bạn bè!");
            }
        } catch (Exception e) {
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

}
