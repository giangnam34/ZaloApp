package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.*;
import com.essay.zaloapp.domain.models.Composite.GroupChatUserId;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatMessageResponse;
import com.essay.zaloapp.domain.payload.response.ChatMessage.FileData;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ReplyMessageResponse;
import com.essay.zaloapp.domain.payload.response.RoomChat.GetAllRoomResponse;
import com.essay.zaloapp.domain.payload.response.RoomChat.LastMessage;
import com.essay.zaloapp.domain.payload.response.RoomChat.StatusOfUser;
import com.essay.zaloapp.domain.payload.response.RoomChat.UserOfRoom;
import com.essay.zaloapp.repository.GroupChatRepository;
import com.essay.zaloapp.repository.GroupChatUserRepository;
import com.essay.zaloapp.repository.MessageChatRepository;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.ChatMessageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {


    @Autowired
    private MessageChatRepository messageChatRepository;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private GroupChatUserRepository groupChatUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public GetAllMessages getAllMessages(Long roomId, Long userId, int page, int size) throws Exception {
        if (!groupChatRepository.existsById(roomId)) throw new Exception("Nhóm chat này không tồn tại!");
        User user = userRepository.findById(userId);
        List<GroupChatUser> groupChatUsersList = groupChatUserRepository.findAllByGroupId(roomId);
        User receiver = new User();
        Boolean check = false;
        for (GroupChatUser gcu : groupChatUsersList) {
            if (gcu.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                check = true;
            } else {
                receiver = userRepository.findByPhoneNumber(gcu.getId().getPhoneNumberUser());
            }
        }
        if (!check) {
            throw new Exception("Người dùng không có quyền xem tin nhắn của nhóm chat này!");
        }

        List<MessageChat> messageChatsForUpdate = messageChatRepository.findAllByGroupIdAndUserPhoneNumber(roomId, receiver.getPhoneNumber());
        for (MessageChat messageChat : messageChatsForUpdate) {
            messageChat.setSeen(true);
            messageChatRepository.save(messageChat);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<MessageChat> messageChatPage = messageChatRepository.findAllByGroupId(roomId, pageable);
        Page<MessageChat> replyMessageChatPage = messageChatRepository.findAllByGroupIdAndReplyMessage(roomId, pageable);

        List<MessageChat> messageChatList = messageChatPage.getContent();
        List<MessageChat> replyMessageChatList = replyMessageChatPage.getContent();

        Map<Long, ChatMessageResponse> responseMap = new HashMap<>();
        List<ChatMessageResponse> responses = new ArrayList<>();

        for (MessageChat messageChat : messageChatList) {
            if (messageChat.getReplyMessage() == null) {
                ChatMessageResponse response = new ChatMessageResponse();
                List<String> reactions = new ArrayList<>();
                response.set_id(messageChat.getId() + "");
                response.setContent(messageChat.getContent());
                User sender = userRepository.findById(messageChat.getUser().getId());
                response.setSenderId(sender.getId() + "");
                response.setUsername(sender.getFullName());
                response.setAvatar("http://localhost:8181/media/getImage/" + sender.getImageAvatarUrl());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(messageChat.getUpdatedAt());
                calendar.add(Calendar.HOUR_OF_DAY, -7);
                Date adjustedDate = calendar.getTime();
                response.setDate(formatDate(adjustedDate));
                response.setTimestamp(extractTime(adjustedDate));

                response.setSystem(messageChat.getIsSystem());
                response.setSaved(messageChat.getSaved());
                response.setDistributed(messageChat.getDistributed());
                response.setSeen(messageChat.getSeen());
                response.setDeleted(messageChat.getDeleted());
                response.setFailure(messageChat.getFailure());
                response.setDisableActions(messageChat.getDisableActions());
                response.setDisableReactions(messageChat.getDisableReactions());
                response.setReactions(reactions);
                response.setReplyMessage(null);
//                response.setFiles(messageChat.getFiles().stream().map(file -> "http://localhost:8181/media/" + (file.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + file.getResourceValue()).collect(Collectors.toList()));

                List<FileData> files = new ArrayList<>();
                FileData fileData = new FileData();
                fileData.setName("response");
                fileData.setSize(Long.valueOf(2200));
                fileData.setAudio(false);
                fileData.setType("jpeg");
                fileData.setDuration(Float.valueOf(0));
                fileData.setPreview("data:image/jpeg;base64");
                fileData.setProgress(Long.valueOf(88));
                fileData.setUrl("http://localhost:8181/media/getImage/" + fileData.getName() + "." + fileData.getType());
                files.add(fileData);
                response.setFiles(files);

                responseMap.put(messageChat.getId(), response);
                responses.add(response);
            }
        }

        for (MessageChat replyMessageChat : replyMessageChatList) {
            ChatMessageResponse parentResponse = responseMap.get(replyMessageChat.getReplyMessage().getId());
            if (parentResponse != null) {
                ReplyMessageResponse replyMessageResponse = new ReplyMessageResponse();

                replyMessageResponse.setContent(replyMessageChat.getContent());
                replyMessageResponse.setSenderId(replyMessageChat.getUser().getId() + "");
                List<FileData> files = new ArrayList<>();
                FileData fileData = new FileData();
//                replyMessageResponse.setFiles(replyMessageChat.getFiles().stream()
//                        .map(file -> "http://localhost:8181/media/" + (file.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + file.getResourceValue())
//                        .collect(Collectors.toList()));
                fileData.setName("hq720");
                fileData.setSize(Long.valueOf(2200));
                fileData.setAudio(false);
                fileData.setType("jpg");
                fileData.setDuration(Float.valueOf(0));
                fileData.setPreview("data:image/jpg;base64");
                fileData.setProgress(Long.valueOf(88));
                fileData.setUrl("http://localhost:8181/media/getImage/" + fileData.getName() + "." + fileData.getType());
                files.add(fileData);
                replyMessageResponse.setFiles(files);

                parentResponse.setReplyMessage(replyMessageResponse);
            }
        }

        Collections.reverse(responses);
        GetAllMessages result = new GetAllMessages("Thành công!", responses, messageChatPage.getTotalPages(), page);
        return result;
    }

    @Override
    public GetAllRooms getAllRooms(Long userId, String sortOrder) throws Exception {
        List<GetAllRoomResponse> responses = new ArrayList<>();
        try {
            User user = userRepository.findById(userId);
            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findByIdPhoneNumberUser(user.getPhoneNumber());

            Comparator<GetAllRoomResponse> groupNameComparator = Comparator.comparing(GetAllRoomResponse::getRoomName);

            for (GroupChatUser groupChatUser : groupChatUsers) {
                GetAllRoomResponse response = new GetAllRoomResponse();

                GroupChat groupChat = groupChatRepository.findById(groupChatUser.getId().getGroupId()).get();
                response.setRoomId(groupChat.getId() + "");
                List<GroupChatUser> groupChatUsersForGetGroupName = groupChatUserRepository.findAllByGroupId(groupChatUser.getId().getGroupId());
                for (GroupChatUser gcu : groupChatUsersForGetGroupName) {
                    User tester = userRepository.findByPhoneNumber(gcu.getId().getPhoneNumberUser());
                    if (tester.getId() != userId) {
                        response.setRoomName(tester.getFullName());
                    }
                }
                response.setAvatar("http://localhost:8181/media/getImage/" + groupChat.getAvatar());

                List<MessageChat> messages = messageChatRepository.findLatestMessageByGroupId(groupChat.getId());
                MessageChat lastMessage = messages.get(0);
                LastMessage lastMessageResponse = new LastMessage();
                lastMessageResponse.set_id(lastMessage.getId() + "");
                lastMessageResponse.setContent(lastMessage.getContent());
                lastMessageResponse.setSenderId(lastMessage.getUser().getId() + "");
                lastMessageResponse.setUsername(lastMessage.getUser().getFullName());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastMessage.getSendAt());
                calendar.add(Calendar.HOUR_OF_DAY, -7);
                Date adjustedDate = calendar.getTime();
                lastMessageResponse.setTimestamp(extractTime(adjustedDate));
                lastMessageResponse.setSaved(lastMessage.getSaved());
                lastMessageResponse.setDistributed(lastMessage.getDistributed());
                lastMessageResponse.setSeen(lastMessage.getSeen());
                lastMessageResponse.setIsNew(true);
                response.setLastMessage(lastMessageResponse);

                List<GroupChatUser> groupChatUserForGetUser = groupChatUserRepository.findAllByGroupId(groupChatUser.getId().getGroupId());
                List<UserOfRoom> userOfRooms = new ArrayList<>();
                User receiver = new User();
                for (GroupChatUser gcu : groupChatUserForGetUser) {
                    User userFound = userRepository.findByPhoneNumber(gcu.getId().getPhoneNumberUser());
                    if (userFound.getId() != userId) {
                        receiver = userFound;
                    }
                    UserOfRoom userOfRoom = new UserOfRoom();
                    userOfRoom.set_id(userFound.getId() + "");
                    userOfRoom.setUsername(userFound.getFullName());
                    userOfRoom.setAvatar("http://localhost:8181/media/getImage/" + userFound.getImageAvatarUrl());

                    StatusOfUser statusOfUser = new StatusOfUser();
                    statusOfUser.setState(userFound.getStatus() + "");
                    calendar.setTime(userFound.getLastActive());
                    calendar.add(Calendar.HOUR_OF_DAY, -7);
                    adjustedDate = calendar.getTime();
                    statusOfUser.setLastChanged(formatDateTime(adjustedDate));
                    userOfRoom.setStatus(statusOfUser);

                    userOfRooms.add(userOfRoom);
                }

                response.setUnreadCount(messageChatRepository.countBySeenFalseAndGroupId(groupChat.getId(), receiver.getPhoneNumber()));

                response.setUsers(userOfRooms);
                List<String> typingUsers = new ArrayList<>();
                response.setTypingUsers(typingUsers);
                responses.add(response);
            }
            if ("desc".equalsIgnoreCase(sortOrder)) {
                responses.sort(groupNameComparator.reversed());
            } else {
                responses.sort(groupNameComparator);
            }
            return new GetAllRooms("Thành công!", responses);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new GetAllRooms("Có lỗi xảy ra trong quá trình thực thi, vui lòng thử lại!", responses);
        }

    }

    @Override
    public String deleteRoom(Long userId, Long roomId) {
        try {
            if (!groupChatRepository.existsById(roomId)) return "Nhóm chat này không tồn tại!";
            User user = userRepository.findById(userId);
            GroupChatUser groupChatUser = new GroupChatUser();
            List<GroupChatUser> groupChatUserList = groupChatUserRepository.findAllByGroupId(roomId);
            boolean check = false;
            for (GroupChatUser gcu : groupChatUserList) {
                if (gcu.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                    check = true;
                    groupChatUser = gcu;
                    break;
                }
            }
            if (!check) {
                return "Người dùng không có quyền thực hiện hành động này!";
            }
            if (!groupChatUser.getIsDeleted()) {
                groupChatUser.setIsDeleted(true);
                groupChatUserRepository.save(groupChatUser);

                Optional<GroupChat> groupChatOptional = groupChatRepository.findById(groupChatUser.getId().getGroupId());
                if (groupChatOptional.isPresent()) {
                    GroupChat groupChat = groupChatOptional.get();
                    groupChat.setDeletedCount(groupChat.getDeletedCount() + 1);
                    if (groupChat.getDeletedCount() == 2) {
                        groupChat.setIsDeleted(true);
                    }
                    groupChatRepository.save(groupChat);
                }
                return "Xóa hội thoại thành công!";
            }
            return "Hành động không hợp lệ!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra. Vui lòng thử lại!";
        }
    }

    @Override
    public String createRoom(Long senderId, Long receiverId) {
        try {
            List<GroupChatUser> testSender = groupChatUserRepository.findAllByPhoneNumberUser(userRepository.findById(senderId).getPhoneNumber());
            List<GroupChatUser> testReceiver = groupChatUserRepository.findAllByPhoneNumberUser(userRepository.findById(receiverId).getPhoneNumber());
            for (GroupChatUser sender : testSender) {
                for (GroupChatUser receiver : testReceiver) {
                    if (sender.getId().getGroupId() == receiver.getId().getGroupId()) {
                        if (sender.getIsDeleted()) {
                            sender.setIsDeleted(false);
                            GroupChat groupChatSender = groupChatRepository.findById(sender.getId().getGroupId()).get();
                            groupChatSender.setDeletedCount(groupChatSender.getDeletedCount() - 1);
                            groupChatRepository.save(groupChatSender);
                        }
                        return "Tạo cuộc hội thoại thành công!";
                    }
                }
            }

            GroupChat groupChat = new GroupChat();
            User sender = userRepository.findById(senderId);
            User receiver = userRepository.findById(receiverId);
            groupChat.setGroupName(receiver.getFullName());
            groupChat.setAvatar(receiver.getImageAvatarUrl());
            groupChat = groupChatRepository.save(groupChat);

            GroupChatUser groupChatSender = new GroupChatUser();
            GroupChatUserId groupChatSenderId = new GroupChatUserId();
            groupChatSenderId.setGroupId(groupChat.getId());
            groupChatSenderId.setPhoneNumberUser(sender.getPhoneNumber());
            groupChatSender.setId(groupChatSenderId);
            groupChatUserRepository.save(groupChatSender);

            GroupChatUser groupChatReceiver = new GroupChatUser();
            GroupChatUserId groupChatReceiverId = new GroupChatUserId();
            groupChatReceiverId.setGroupId(groupChat.getId());
            groupChatReceiverId.setPhoneNumberUser(receiver.getPhoneNumber());
            groupChatReceiver.setId(groupChatReceiverId);
            groupChatUserRepository.save(groupChatReceiver);

            return "Tạo cuộc hội thoại thành công!";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "Có lỗi trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String deleteMessage(Long userId, Long messageId) {
        try {
            if (!messageChatRepository.existsByIdAndNotDeleted(messageId)) return "Tin nhắn không tồn tại!";
            MessageChat messageChat = messageChatRepository.findByIdAndNotDeleted(messageId).get();
            if (messageChat.getUser().getId() != userId) {
                return "Bạn không có quyền xóa tin nhắn này!";
            }
            messageChat.setDeleted(true);
            messageChatRepository.save(messageChat);
            return "Xóa tin nhắn thành công!";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!";
        }
    }

    public String formatDate(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
        return outputFormat.format(date);
    }

    public String extractTime(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        return outputFormat.format(date);
    }

    public String formatDateTime(Date date) {
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());

        if (dateTime.toLocalDate().isEqual(currentDate)) {
            DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("'today,' HH:mm");
            return dateTime.format(todayFormat);
        } else {
            DateTimeFormatter otherFormat = DateTimeFormatter.ofPattern("dd MMMM, HH:mm");
            return dateTime.format(otherFormat);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllMessages {
        private String message;
        private List<ChatMessageResponse> chatMessageResponses;
        private int totalPages;
        private int currentPage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllRooms {
        private String message;
        private List<GetAllRoomResponse> getAllRoomResponses;
    }
}
