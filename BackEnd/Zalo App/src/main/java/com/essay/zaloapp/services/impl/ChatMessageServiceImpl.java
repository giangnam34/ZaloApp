package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.ResourceType;
import com.essay.zaloapp.domain.models.GroupChat;
import com.essay.zaloapp.domain.models.GroupChatUser;
import com.essay.zaloapp.domain.models.MessageChat;
import com.essay.zaloapp.domain.models.User;
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
import java.util.stream.Collectors;

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
        Boolean check = false;
        for (GroupChatUser gcu : groupChatUsersList) {
            if (gcu.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                check = true;
            }
        }
        if (!check) {
            throw new Exception("Người dùng không có quyền xem tin nhắn của nhóm chat này!");
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

        GetAllMessages result = new GetAllMessages("Thành công!", responses, messageChatPage.getTotalPages(), page);
        return result;
    }

    @Override
    public GetAllRooms getAllRooms(Long userId, String sortOrder) throws Exception {
        User user = userRepository.findById(userId);
        List<GroupChatUser> groupChatUsers = groupChatUserRepository.findByIdPhoneNumberUser(user.getPhoneNumber());
        List<GetAllRoomResponse> responses = new ArrayList<>();

        Comparator<GetAllRoomResponse> groupNameComparator = Comparator.comparing(GetAllRoomResponse::getRoomName);

        for (GroupChatUser groupChatUser : groupChatUsers) {
            GetAllRoomResponse response = new GetAllRoomResponse();

            GroupChat groupChat = groupChatRepository.findById(groupChatUser.getId().getGroupId()).get();
            response.setRoomId(groupChat.getId() + "");
            response.setRoomName(groupChat.getGroupName());
            response.setAvatar("http://localhost:8181/media/getImage/" + groupChat.getAvatar());
            response.setUnreadCount(messageChatRepository.countBySeenFalseAndGroupId(groupChat.getId()));

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
            for (GroupChatUser gcu : groupChatUserForGetUser) {
                User userFound = userRepository.findByPhoneNumber(gcu.getId().getPhoneNumberUser());
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
