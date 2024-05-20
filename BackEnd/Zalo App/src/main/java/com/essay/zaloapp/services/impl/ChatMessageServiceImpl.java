package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.ResourceType;
import com.essay.zaloapp.domain.models.*;
import com.essay.zaloapp.domain.models.Composite.GroupChatUserId;
import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewChatMessageRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.UpdateChatMessageRequest;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatMessageResponse;
import com.essay.zaloapp.domain.payload.response.ChatMessage.FileData;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ReplyMessageResponse;
import com.essay.zaloapp.domain.payload.response.RoomChat.GetAllRoomResponse;
import com.essay.zaloapp.domain.payload.response.RoomChat.LastMessage;
import com.essay.zaloapp.domain.payload.response.RoomChat.StatusOfUser;
import com.essay.zaloapp.domain.payload.response.RoomChat.UserOfRoom;
import com.essay.zaloapp.repository.*;
import com.essay.zaloapp.services.ChatMessageService;
import com.essay.zaloapp.services.FileStorageService;
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

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private FileStorageService fileStorageService;

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

        List<MessageChat> messageChatList = messageChatPage.getContent();

        Map<Long, ChatMessageResponse> responseMap = new HashMap<>();
        List<ChatMessageResponse> responses = new ArrayList<>();

        for (MessageChat messageChat : messageChatList) {

            ChatMessageResponse response = new ChatMessageResponse();
            response.set_id(messageChat.getId() + "");
            response.setContent(messageChat.getContent());
            User sender = userRepository.findById(messageChat.getUser().getId());
            response.setSenderId(sender.getId() + "");
            response.setUsername(sender.getFullName());
            response.setAvatar("http://localhost:8181/media/getImage/" + sender.getImageAvatarUrl());

//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(messageChat.getUpdatedAt());
//            calendar.add(Calendar.HOUR_OF_DAY, -7);
//            Date adjustedDate = calendar.getTime();
//            response.setDate(formatDate(adjustedDate));
//            response.setTimestamp(extractTime(adjustedDate));
            response.setDate(formatDate(messageChat.getSendAt()));
            response.setTimestamp(extractTime(messageChat.getSendAt()));

            response.setSystem(messageChat.getIsSystem());
            response.setSaved(messageChat.getSaved());
            response.setDistributed(messageChat.getDistributed());
            response.setSeen(messageChat.getSeen());
            response.setDeleted(messageChat.getDeleted());
            response.setFailure(messageChat.getFailure());
            response.setDisableActions(messageChat.getDisableActions());
            response.setDisableReactions(messageChat.getDisableReactions());

            MessageChat messageChatForGetReactions = messageChatRepository.findByIdWithReactions(messageChat.getId());
            Map<String, List<String>> reactions = messageChatForGetReactions.getReactions().stream()
                    .collect(Collectors.groupingBy(
                            Reaction::getEmoji,
                            Collectors.mapping(reaction -> reaction.getUser().getId().toString(), Collectors.toList())
                    ));
            response.setReactions(reactions);
            if (messageChat.getReplyMessage() == null) {
                response.setReplyMessage(null);
            } else {
                MessageChat messageChatForReplyMessage = messageChatRepository.findById(messageChat.getReplyMessage().getId()).get();
                ReplyMessageResponse replyMessageResponse = new ReplyMessageResponse();
                replyMessageResponse.setContent(messageChatForReplyMessage.getContent());
                replyMessageResponse.setSenderId(messageChatForReplyMessage.getUser().getId() + "");
                List<FileData> files = new ArrayList<>();
                for (Resource resource : messageChatForReplyMessage.getFiles()) {
                    FileData fileData = new FileData();
                    fileData.setProgress(Long.valueOf(100));
                    fileData.setAudio(false);
                    fileData.setSize(Long.valueOf(2200));
                    fileData.setDuration(Float.valueOf(0));
                    fileData.setUrl("http://localhost:8181/media/" + (resource.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + resource.getResourceValue());
                    String[] parts = resource.getResourceValue().split("\\.(?=[^\\.]+$)");
                    String namePart = "";
                    String extensionPart = "";
                    if (parts.length == 2) {
                        namePart += parts[0];
                        extensionPart += parts[1];
                    }
                    fileData.setName(namePart);
                    fileData.setType(extensionPart);
                    fileData.setPreview("data" + resource.getResourceType() + "/" + extensionPart + ";base64");
                    files.add(fileData);
                }
                replyMessageResponse.setFiles(files);
                response.setReplyMessage(replyMessageResponse);
            }

            List<FileData> files = new ArrayList<>();
            for (Resource resource : messageChat.getFiles()) {
                FileData fileData = new FileData();
                fileData.setProgress(Long.valueOf(100));
                fileData.setAudio(false);
                fileData.setSize(Long.valueOf(2200));
                fileData.setDuration(Float.valueOf(0));
                fileData.setUrl("http://localhost:8181/media/" + (resource.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + resource.getResourceValue());
                String[] parts = resource.getResourceValue().split("\\.(?=[^\\.]+$)");
                String namePart = "";
                String extensionPart = "";
                if (parts.length == 2) {
                    namePart += parts[0];
                    extensionPart += parts[1];
                }
                fileData.setName(namePart);
                fileData.setType(extensionPart);
                fileData.setPreview("data:" + resource.getResourceType() + "/" + extensionPart + ";base64");
                files.add(fileData);
            }
            response.setFiles(files);

            responseMap.put(messageChat.getId(), response);
            responses.add(response);

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
                    if (groupChat.getDeletedCount() == groupChatUserList.size()) {
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
    public String createRoom(Long senderId, List<Long> receiverIds) {
        try {
            if (receiverIds.size() == 0) {
                return "Vui lòng gửi danh sách thành viên của phòng!";
            }


            User sender = userRepository.findById(senderId);
            if (sender == null) return "Người gửi không tồn tại!";

            for (Long receiverId : receiverIds) {

                if (receiverId == senderId) {
                    return "Không thể tự tạo phòng với chính bản thân!";
                }
                if (!userRepository.existsUserById(receiverId)) {
                    return "Người nhận với ID " + receiverId + " không tồn tại!";
                }
            }

            if (receiverIds.size() == 1) {
                Long receiverId = receiverIds.get(0);
                String result = checkAndUpdateExistingChat(senderId, receiverId);
                return result;

            }

            List<GroupChatUser> testSender = groupChatUserRepository.findAllByPhoneNumberUser(sender.getPhoneNumber());
            GroupChat groupChat = findExistingGroupChat(testSender, receiverIds);
            boolean existingChat = groupChat != null;

            if (!existingChat) {
                groupChat = createNewGroupChat(receiverIds);
            }

            addUsersToGroupChat(groupChat, sender, receiverIds, existingChat);
            return "Tạo cuộc hội thoại thành công!";

        } catch (
                Exception e) {
            e.printStackTrace();
            return "Có lỗi trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    private String checkAndUpdateExistingChat(Long senderId, Long receiverId) {
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

    private GroupChat findExistingGroupChat(List<GroupChatUser> testSender, List<Long> receiverIds) {
        outerLoop:
        for (GroupChatUser senderChatUser : testSender) {
            GroupChat groupChatSender = groupChatRepository.findById(senderChatUser.getId().getGroupId()).orElse(null);
            if (groupChatSender == null) continue;

            for (Long receiverId : receiverIds) {
                User receiver = userRepository.findById(receiverId);
                if (receiver == null) continue;

                List<GroupChatUser> testReceiver = groupChatUserRepository.findAllByPhoneNumberUser(receiver.getPhoneNumber());
                boolean receiverInChat = testReceiver.stream()
                        .anyMatch(receiverChatUser -> receiverChatUser.getId().getGroupId().equals(groupChatSender.getId()));

                if (!receiverInChat) {
                    continue outerLoop;
                }
            }

            return groupChatSender;
        }
        return null;
    }

    private GroupChat createNewGroupChat(List<Long> receiverIds) {
        GroupChat groupChat = new GroupChat();
        User firstReceiver = userRepository.findById(receiverIds.get(0));
        if (firstReceiver == null) throw new IllegalArgumentException("Người nhận đầu tiên không tồn tại!");

        groupChat.setGroupName(firstReceiver.getFullName());
        groupChat.setAvatar(firstReceiver.getImageAvatarUrl());
        return groupChatRepository.save(groupChat);
    }

    private void addUsersToGroupChat(GroupChat groupChat, User sender, List<Long> receiverIds, boolean existingChat) {
        if (!existingChat || !isUserInGroupChat(sender.getPhoneNumber(), groupChat.getId())) {
            GroupChatUser groupChatSender = new GroupChatUser();
            GroupChatUserId groupChatSenderId = new GroupChatUserId();
            groupChatSenderId.setGroupId(groupChat.getId());
            groupChatSenderId.setPhoneNumberUser(sender.getPhoneNumber());
            groupChatSender.setId(groupChatSenderId);
            groupChatUserRepository.save(groupChatSender);
        }

        for (Long receiverId : receiverIds) {
            User receiver = userRepository.findById(receiverId);
            if (receiver == null) continue;

            if (!isUserInGroupChat(receiver.getPhoneNumber(), groupChat.getId())) {
                GroupChatUser groupChatReceiver = new GroupChatUser();
                GroupChatUserId groupChatReceiverId = new GroupChatUserId();
                groupChatReceiverId.setGroupId(groupChat.getId());
                groupChatReceiverId.setPhoneNumberUser(receiver.getPhoneNumber());
                groupChatReceiver.setId(groupChatReceiverId);
                groupChatUserRepository.save(groupChatReceiver);
            }
        }
    }

    private boolean isUserInGroupChat(String phoneNumber, Long groupId) {
        List<GroupChatUser> users = groupChatUserRepository.findAllByPhoneNumberUser(phoneNumber);
        return users.stream().anyMatch(user -> user.getId().getGroupId().equals(groupId));
    }

    @Override
    public GetAMessage createChatMessage(Long userId, AddNewChatMessageRequest addNewChatMessageRequest) {
        try {
            User user = userRepository.findById(userId);
            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(addNewChatMessageRequest.getRoomId());
            if (groupChatUsers.isEmpty()) {

                return new GetAMessage("Mã phòng không tồn tại!", new ChatMessageResponse());
            }
            Boolean check = false;
            for (GroupChatUser groupChatUser : groupChatUsers) {
                if (groupChatUser.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                return new GetAMessage("Người dùng không có quyền gửi tin nhắn trong phòng này!", new ChatMessageResponse());
            }

            MessageChat messageChat = new MessageChat();
            messageChat.setUser(user);
            if (addNewChatMessageRequest.getContent() != null) {
                messageChat.setContent(addNewChatMessageRequest.getContent());
            }
            if (addNewChatMessageRequest.getDisableActions() != null) {
                messageChat.setDisableActions(addNewChatMessageRequest.getDisableActions());
            }
            if (addNewChatMessageRequest.getDisableReactions() != null) {
                messageChat.setDisableReactions(addNewChatMessageRequest.getDisableReactions());
            }
            if (addNewChatMessageRequest.getDistributed() != null) {
                messageChat.setDistributed(addNewChatMessageRequest.getDistributed());
            }
            if (addNewChatMessageRequest.getFailure() != null) {
                messageChat.setFailure(addNewChatMessageRequest.getFailure());
            }
            if (addNewChatMessageRequest.getSystem() != null) {
                messageChat.setIsSystem(addNewChatMessageRequest.getSystem());
            }
            if (addNewChatMessageRequest.getSaved() != null) {
                messageChat.setSaved(addNewChatMessageRequest.getSaved());
            }
            if (addNewChatMessageRequest.getSeen() != null) {
                messageChat.setSeen(addNewChatMessageRequest.getSeen());
            }
            messageChat.setSendAt(new Date(new Date().getTime()));
            messageChat.setUpdatedAt(new Date(new Date().getTime()));

            if (!groupChatRepository.existsById(addNewChatMessageRequest.getRoomId())) {
                return new GetAMessage("Phòng chat này không tồn tại!", new ChatMessageResponse());
            }
            GroupChat groupChat = groupChatRepository.findById(addNewChatMessageRequest.getRoomId()).get();
            messageChat.setGroupChat(groupChat);

            if (addNewChatMessageRequest.getReplyMessageId() != null) {
                Optional<MessageChat> repliedMessageOptional = messageChatRepository.findById(addNewChatMessageRequest.getReplyMessageId());
                if (repliedMessageOptional.isEmpty()) {
                    return new GetAMessage("Tin nhắn được trả lời không tồn tại!", new ChatMessageResponse());
                }
                MessageChat repliedMessage = repliedMessageOptional.get();
                messageChat.setReplyMessage(repliedMessage);
            }

            List<Resource> resourceList = new ArrayList<>();
            if (addNewChatMessageRequest.getFiles() != null && !addNewChatMessageRequest.getFiles()[0].isEmpty()) {
                resourceList = Arrays.stream(addNewChatMessageRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)).collect(Collectors.toList());
            }
            messageChat.setFiles(resourceList);
            messageChat = messageChatRepository.save(messageChat);

            ChatMessageResponse response = new ChatMessageResponse();
            response.set_id(messageChat.getId() + "");
            response.setContent(messageChat.getContent());
            User sender = userRepository.findById(messageChat.getUser().getId());
            response.setSenderId(sender.getId() + "");
            response.setUsername(sender.getFullName());
            response.setAvatar("http://localhost:8181/media/getImage/" + sender.getImageAvatarUrl());
            response.setDate(formatDate(messageChat.getSendAt()));
            response.setTimestamp(extractTime(messageChat.getSendAt()));

            response.setSystem(messageChat.getIsSystem());
            response.setSaved(messageChat.getSaved());
            response.setDistributed(messageChat.getDistributed());
            response.setSeen(messageChat.getSeen());
            response.setDeleted(messageChat.getDeleted());
            response.setFailure(messageChat.getFailure());
            response.setDisableActions(messageChat.getDisableActions());
            response.setDisableReactions(messageChat.getDisableReactions());
            response.setReactions(null);
            
            if (messageChat.getReplyMessage() == null) {
                response.setReplyMessage(null);
            } else {
                MessageChat messageChatForReplyMessage = messageChatRepository.findById(messageChat.getReplyMessage().getId()).get();
                ReplyMessageResponse replyMessageResponse = new ReplyMessageResponse();
                replyMessageResponse.setContent(messageChatForReplyMessage.getContent());
                replyMessageResponse.setSenderId(messageChatForReplyMessage.getUser().getId() + "");
                List<FileData> files = new ArrayList<>();
                for (Resource resource : messageChatForReplyMessage.getFiles()) {
                    FileData fileData = new FileData();
                    fileData.setProgress(Long.valueOf(100));
                    fileData.setAudio(false);
                    fileData.setSize(Long.valueOf(2200));
                    fileData.setDuration(Float.valueOf(0));
                    fileData.setUrl("http://localhost:8181/media/" + (resource.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + resource.getResourceValue());
                    String[] parts = resource.getResourceValue().split("\\.(?=[^\\.]+$)");
                    String namePart = "";
                    String extensionPart = "";
                    if (parts.length == 2) {
                        namePart += parts[0];
                        extensionPart += parts[1];
                    }
                    fileData.setName(namePart);
                    fileData.setType(extensionPart);
                    fileData.setPreview("data" + resource.getResourceType() + "/" + extensionPart + ";base64");
                    files.add(fileData);
                }
                replyMessageResponse.setFiles(files);
                response.setReplyMessage(replyMessageResponse);
            }

            List<FileData> files = new ArrayList<>();
            for (Resource resource : messageChat.getFiles()) {
                FileData fileData = new FileData();
                fileData.setProgress(Long.valueOf(100));
                fileData.setAudio(false);
                fileData.setSize(Long.valueOf(2200));
                fileData.setDuration(Float.valueOf(0));
                fileData.setUrl("http://localhost:8181/media/" + (resource.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + resource.getResourceValue());
                String[] parts = resource.getResourceValue().split("\\.(?=[^\\.]+$)");
                String namePart = "";
                String extensionPart = "";
                if (parts.length == 2) {
                    namePart += parts[0];
                    extensionPart += parts[1];
                }
                fileData.setName(namePart);
                fileData.setType(extensionPart);
                fileData.setPreview("data:" + resource.getResourceType() + "/" + extensionPart + ";base64");
                files.add(fileData);
            }
            response.setFiles(files);
            return new GetAMessage("Tin nhắn đã được gửi!", response);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new GetAMessage("Đã xảy ra lỗi trong quá trình thực hiện, vui lòng thử lại sau!", new ChatMessageResponse());
        }
    }

    @Override
    public String updateChatMessage(Long userId, UpdateChatMessageRequest updateChatMessageRequest) {
        try {
            User user = userRepository.findById(userId);
            Optional<MessageChat> messageChatOptional = messageChatRepository.findById(updateChatMessageRequest.getMessageId());
            if (messageChatOptional.isEmpty()) {
                return "Tin nhắn không tồn tại!";
            }
            MessageChat messageChat = messageChatOptional.get();
            if (user.getId() != messageChat.getUser().getId()) {
                if (updateChatMessageRequest.getReactions() != null) {
                    Map<String, List<String>> reactionsMap = updateChatMessageRequest.getReactionsMap();
                    reactionRepository.deleteByMessageChat(messageChat);
                    List<Reaction> newReactions = new ArrayList<>();
                    for (Map.Entry<String, List<String>> entry : reactionsMap.entrySet()) {
                        String emoji = entry.getKey();
                        for (String userIdStr : entry.getValue()) {
                            Long reactingUserId = Long.valueOf(userIdStr);
                            if (!userRepository.existsUserById(reactingUserId)) {
                                return "Người dùng không tồn tại!";
                            }
                            User reactingUser = userRepository.findById(reactingUserId);
                            if (reactingUser != null) {
                                Reaction reaction = new Reaction();
                                reaction.setEmoji(emoji);
                                reaction.setUser(reactingUser);
                                reaction.setMessageChat(messageChat);
                                newReactions.add(reaction);
                            }
                        }
                    }
                    reactionRepository.saveAll(newReactions);
                    messageChatRepository.save(messageChat);
                    return "Cập nhật tin nhắn thành công!";
                }
                return "Người dùng không có quyền cập nhật tin nhắn này!";
            }

            if (updateChatMessageRequest.getContent() != null) {
                messageChat.setContent(updateChatMessageRequest.getContent());
            }
            if (updateChatMessageRequest.getDisableActions() != null) {
                messageChat.setDisableActions(updateChatMessageRequest.getDisableActions());
            }
            if (updateChatMessageRequest.getDisableReactions() != null) {
                messageChat.setDisableReactions(updateChatMessageRequest.getDisableReactions());
            }
            if (updateChatMessageRequest.getDistributed() != null) {
                messageChat.setDistributed(updateChatMessageRequest.getDistributed());
            }
            if (updateChatMessageRequest.getFailure() != null) {
                messageChat.setFailure(updateChatMessageRequest.getFailure());
            }
            if (updateChatMessageRequest.getSystem() != null) {
                messageChat.setIsSystem(updateChatMessageRequest.getSystem());
            }
            if (updateChatMessageRequest.getSaved() != null) {
                messageChat.setSaved(updateChatMessageRequest.getSaved());
            }
            if (updateChatMessageRequest.getSeen() != null) {
                messageChat.setSeen(updateChatMessageRequest.getSeen());
            }
            messageChat.setUpdatedAt(new Date(new Date().getTime()));

            if (updateChatMessageRequest.getFiles() != null) {
                if (updateChatMessageRequest.getFiles()[0].isEmpty()) messageChat.setFiles(new ArrayList<>());
                else {
                    List<Resource> resourceList = Arrays.stream(updateChatMessageRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)).collect(Collectors.toList());
                    messageChat.setFiles(resourceList);
                }
            }

            if (updateChatMessageRequest.getReplyMessageId() != null) {
                Optional<MessageChat> repliedMessageOptional = messageChatRepository.findById(updateChatMessageRequest.getReplyMessageId());
                if (repliedMessageOptional.isEmpty()) {
                    return "Tin nhắn được trả lời không tồn tại!";
                }
                MessageChat repliedMessage = repliedMessageOptional.get();
                messageChat.setReplyMessage(repliedMessage);
            }

            if (updateChatMessageRequest.getReactions() != null) {
                Map<String, List<String>> reactionsMap = updateChatMessageRequest.getReactionsMap();
                reactionRepository.deleteByMessageChat(messageChat);
                List<Reaction> newReactions = new ArrayList<>();
                for (Map.Entry<String, List<String>> entry : reactionsMap.entrySet()) {
                    String emoji = entry.getKey();
                    for (String userIdStr : entry.getValue()) {
                        Long reactingUserId = Long.valueOf(userIdStr);
                        if (!userRepository.existsUserById(reactingUserId)) {
                            return "Người dùng không tồn tại!";
                        }
                        User reactingUser = userRepository.findById(reactingUserId);
                        if (reactingUser != null) {
                            Reaction reaction = new Reaction();
                            reaction.setEmoji(emoji);
                            reaction.setUser(reactingUser);
                            reaction.setMessageChat(messageChat);
                            newReactions.add(reaction);
                        }
                    }
                }
                reactionRepository.saveAll(newReactions);
            }

            messageChatRepository.save(messageChat);
            return "Cập nhật tin nhắn thành công!";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "Đã xảy ra lỗi trong quá trình thực thi, vui lòng kiểm tra lại!";
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
    public class GetAMessage{
        private String message;
        private ChatMessageResponse chatMessageResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllRooms {
        private String message;
        private List<GetAllRoomResponse> getAllRoomResponses;
    }
}
