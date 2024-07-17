package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.ResourceType;
import com.essay.zaloapp.domain.enums.TypeNotification;
import com.essay.zaloapp.domain.models.*;
import com.essay.zaloapp.domain.models.Composite.GroupChatUserId;
import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewChatMessageRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewRoomRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.SendNotificationRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.UpdateChatMessageRequest;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatMessageResponse;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatNotification;
import com.essay.zaloapp.domain.payload.response.ChatMessage.FileData;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ReplyMessageResponse;
import com.essay.zaloapp.domain.payload.response.RoomChat.*;
import com.essay.zaloapp.repository.*;
import com.essay.zaloapp.services.ChatMessageService;
import com.essay.zaloapp.services.FileStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

        Date joinDate = null;
        for (GroupChatUser gcu : groupChatUsersList) {
            if (gcu.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                joinDate = gcu.getJoinedAt();
                break;
            }
        }
        if (joinDate == null) {
            throw new Exception("Không tìm thấy ngày tham gia của người dùng!");
        }

        Date joinedDate = new Date(joinDate.getTime() - (24 * 60 * 60 * 1000));

        Pageable pageable = PageRequest.of(page, size);
        Page<MessageChat> messageChatPage;

        if (groupChatUsersList.size() >= 3) {
            messageChatPage = messageChatRepository.findAllByGroupIdAndSendAtAfter(roomId, joinedDate, pageable);
        } else {
            messageChatPage = messageChatRepository.findAllByGroupId(roomId, pageable);
        }

        List<MessageChat> messageChatList = messageChatPage.getContent();

        Map<Long, ChatMessageResponse> responseMap = new HashMap<>();
        List<ChatMessageResponse> responses = new ArrayList<>();

        for (MessageChat messageChat : messageChatList) {
            ChatMessageResponse response = new ChatMessageResponse();
            response.set_id(messageChat.getId() + "");
            response.setContent(messageChat.getContent());
            User sender = userRepository.findById(messageChat.getUser().getId());

            boolean isBlocked = friendsRepository.existsByUser1AndUser2AndIsBlock(user, sender, userId);

            response.setIsBlock(isBlocked);

            response.setSenderId(sender.getId() + "");
            response.setUsername(sender.getFullName());
            response.setAvatar("http://localhost:8181/media/getImage/" + sender.getImageAvatarUrl());
            response.setDate(formatDate(messageChat.getSendAt()));
            response.setTimestamp(extractTime(messageChat.getSendAt()));
            response.setSystem(messageChat.getIsSystem());
            response.setSaved(messageChat.getSaved());
            response.setDistributed(true);
            response.setSeen(messageChat.getUser().getId() != userId ? true : messageChat.getSeen());
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
                    fileData.setPreview("data:" + resource.getResourceType() + "/" + extensionPart + ";base64");
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

            List<UserOfRoom> taggedUsers = new ArrayList<>();
            if (messageChat.getTagUsers() != null) {
                for (User taggedUser : messageChat.getTagUsers()) {
                    UserOfRoom getTaggedUser = new UserOfRoom();
                    getTaggedUser.set_id(taggedUser.getId() + "");
                    getTaggedUser.setAvatar("http://localhost:8181/media/getImage/" + taggedUser.getImageAvatarUrl());
                    StatusOfUser status = new StatusOfUser();
                    status.setState(taggedUser.getStatus() + "");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(taggedUser.getLastActive());
                    calendar.add(Calendar.HOUR_OF_DAY, -7);
                    Date adjustedDate = calendar.getTime();
                    status.setLastChanged(formatDateTime(adjustedDate));
                    getTaggedUser.setStatus(status);
                    getTaggedUser.setUsername(taggedUser.getFullName());
                    taggedUsers.add(getTaggedUser);
                }
            }
            response.setTaggedUser(taggedUsers);

            responseMap.put(messageChat.getId(), response);
            responses.add(response);
            if (!messageChat.getSeen() && !Objects.equals(messageChat.getUser().getId(), userId)) {
                messageChat.setSeen(true);
                messageChatRepository.save(messageChat);
                notifyToUser(messageChat.getUser().getId(), ChatNotification.builder().roomId(messageChat.getGroupChat().getId()).typeNotification(TypeNotification.UPDATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
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
                        if (groupChatUsersForGetGroupName.size() <= 2) {
                            response.setRoomName(tester.getFullName());
                            response.setAvatar("http://localhost:8181/media/getImage/" + tester.getImageAvatarUrl());
                            break;
                        }
                    }
                }
                if (groupChatUsersForGetGroupName.size() > 2) {
                    response.setAvatar("http://localhost:8181/media/getImage/" + groupChat.getAvatar());
                    response.setRoomName(groupChat.getGroupName());
                }
                Calendar calendar = Calendar.getInstance();
                Date adjustedDate;
                List<MessageChat> messages = messageChatRepository.findLatestMessageByGroupId(groupChat.getId());
                if (!messages.isEmpty()) {
                    MessageChat lastMessage = messages.get(0);
                    LastMessage lastMessageResponse = new LastMessage();
                    lastMessageResponse.set_id(lastMessage.getId() + "");
                    lastMessageResponse.setContent(lastMessage.getContent());
                    if (!lastMessage.getIsSystem()) {
                        lastMessageResponse.setSenderId(lastMessage.getUser().getId() + "");
                        lastMessageResponse.setUsername(lastMessage.getUser().getFullName());
                    }
                    calendar.setTime(lastMessage.getSendAt());
                    calendar.add(Calendar.HOUR_OF_DAY, -7);
                    adjustedDate = calendar.getTime();
                    lastMessageResponse.setTimestamp(extractTime(lastMessage.getSendAt()));
                    lastMessageResponse.setSaved(lastMessage.getSaved());
                    lastMessageResponse.setDistributed(true);
                    lastMessageResponse.setSeen(lastMessage.getSeen());
                    lastMessageResponse.setIsNew(true);
                    if (lastMessage.getDeleted()) {
                        lastMessageResponse.setContent("⛔ This message was deleted");
                    } else if ((lastMessage.getContent().isEmpty() || lastMessage.getContent() == null) && lastMessage.getFiles() != null && !lastMessage.getFiles().isEmpty()) {
                        lastMessageResponse.setContent("\uD83D\uDCF9 Media content");
                    }
                    response.setLastMessage(lastMessageResponse);
                } else {
                    response.setLastMessage(null);
                }


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
                    userOfRoom.setPhoneNumber(userFound.getPhoneNumber());

                    StatusOfUser statusOfUser = new StatusOfUser();
                    statusOfUser.setState(String.valueOf(userFound.getStatus()).toLowerCase());
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
    public List<Long> getListIdsByListPhoneNumber(List<String> usersPhoneNumber) throws Exception {
        List<Long> ids = new ArrayList<>();
        for (String phoneNumber : usersPhoneNumber) {
            if (!userRepository.existsUserByPhoneNumber(phoneNumber)) {
                throw new Exception("Người dùng không tồn tại!");
            }
            User user = userRepository.findByPhoneNumber(phoneNumber);
            ids.add(user.getId());
        }
        return ids;
    }

    @Override
    public GetARoomInfo getRoomInfo(Long userId, Long roomId) throws Exception {
        GetRoomInfo response = new GetRoomInfo();
        try {
            if (!groupChatRepository.existsById(roomId)) {
                return new GetARoomInfo("Phòng chat không tồn tại!", response);
            }
            GroupChat groupChat = groupChatRepository.findById(roomId).get();
            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(roomId);
            List<UserOfRoom> userOfRoomList = new ArrayList<>();
            int membersCount = groupChatUsers.size();
            Calendar calendar = Calendar.getInstance();
            Date adjustedDate;
            for (GroupChatUser groupChatUser : groupChatUsers) {
                if (membersCount == 2) {
                    User userFound = userRepository.findByPhoneNumber(groupChatUser.getId().getPhoneNumberUser());
                    if (userFound.getId() != userId) {
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

                        userOfRoomList.add(userOfRoom);
                        break;
                    }
                } else {
                    User userFound = userRepository.findByPhoneNumber(groupChatUser.getId().getPhoneNumberUser());
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

                    userOfRoomList.add(userOfRoom);
                }
            }

            response.setRoomId(roomId + "");
            response.setMembersCount(membersCount);
            response.setRoomName(groupChat.getGroupName());
            response.setUsers(userOfRoomList);
            response.setRoomAvatar("http://localhost:8181/media/getImage/" + groupChat.getAvatar());
            return new GetARoomInfo("Lấy thông tin phòng thành công!", response);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new GetARoomInfo("Có lỗi xảy ra trong quá trình thực thi, vui lòng thử lại!", response);
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
                    MessageChat messageChat = new MessageChat();
                    messageChat.setUser(user);
                    messageChat.setSeen(true);
                    messageChat.setGroupChat(groupChat);
                    messageChat.setIsSystem(true);
                    messageChat.setContent(String.format("%s vừa rời khỏi nhóm", user.getFullName()));
                    messageChat.setSendAt(new Date(new Date().getTime()));
                    messageChat.setUpdatedAt(new Date(new Date().getTime()));
                    messageChatRepository.save(messageChat);
                    groupChatUserRepository.save(groupChatUser);
                    List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(groupChat.getId());
                    for (GroupChatUser userGroup : groupChatUsers) {
                        notifyToUser(userRepository.findByPhoneNumber(userGroup.getId().getPhoneNumberUser()).getId(), ChatNotification.builder().roomId(messageChat.getGroupChat().getId()).typeNotification(TypeNotification.CREATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                    }
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
    public String createRoom(Long senderId, AddNewRoomRequest addNewRoomRequest) {
        try {
            if (addNewRoomRequest.getReceiverIds().size() == 0) {
                return "Vui lòng gửi danh sách thành viên của phòng!";
            }


            User sender = userRepository.findById(senderId);
            if (sender == null) return "Người gửi không tồn tại!";

            for (Long receiverId : addNewRoomRequest.getReceiverIds()) {

                if (receiverId == senderId) {
                    return "Không thể tự tạo phòng với chính bản thân!";
                }
                if (!userRepository.existsUserById(receiverId)) {
                    return "Người nhận với ID " + receiverId + " không tồn tại!";
                }
            }

            if (addNewRoomRequest.getReceiverIds().size() == 1) {
                Long receiverId = addNewRoomRequest.getReceiverIds().get(0);
                String result = checkAndUpdateExistingChat(senderId, receiverId);
                return result;

            }

            List<GroupChatUser> testSender = groupChatUserRepository.findAllByPhoneNumberUser(sender.getPhoneNumber());
            GroupChat groupChat = findExistingGroupChat(testSender, addNewRoomRequest.getReceiverIds());
            boolean existingChat = groupChat != null;

            if (!existingChat) {
                groupChat = createNewGroupChat(addNewRoomRequest);
            } else {

                return "Nhóm đã tồn tại!";
            }

            groupChatRepository.save(groupChat);
            addUsersToGroupChat(groupChat, sender, addNewRoomRequest.getReceiverIds(), existingChat);
            return "Tạo nhóm thành công!";

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
                        if (groupChatUserRepository.findAllByGroupId(sender.getId().getGroupId()) != null
                                && groupChatUserRepository.findAllByGroupId(sender.getId().getGroupId()).size() == 2) {
                            if (sender.getIsDeleted()) {
                                sender.setIsDeleted(false);
                                GroupChat groupChatSender = groupChatRepository.findById(sender.getId().getGroupId()).get();
                                groupChatSender.setDeletedCount(groupChatSender.getDeletedCount() - 1);


                                groupChatRepository.save(groupChatSender);
                            }

                            List<Long> groupChatIds = new ArrayList<>();

                            for (GroupChatUser groupChatUser1 : testSender) {
                                for (GroupChatUser groupChatUser2 : testReceiver) {
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
                                    if (message.getIsSystem() && message.getContent().contains("Không thể tiếp tục gửi tin nhắn do hai người chưa là bạn bè")) {
                                        messageChatRepository.delete(message);
                                    }
                                }
                            }
                            return "Tạo cuộc hội thoại thành công!";
                        }
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
            groupChatSender.setJoinedAt(new Date(new Date().getTime()));
            groupChatUserRepository.save(groupChatSender);

            GroupChatUser groupChatReceiver = new GroupChatUser();
            GroupChatUserId groupChatReceiverId = new GroupChatUserId();
            groupChatReceiverId.setGroupId(groupChat.getId());
            groupChatReceiverId.setPhoneNumberUser(receiver.getPhoneNumber());
            groupChatReceiver.setId(groupChatReceiverId);
            groupChatReceiver.setJoinedAt(new Date(new Date().getTime()));
            groupChatUserRepository.save(groupChatReceiver);

            return "Tạo cuộc hội thoại thành công!";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "Có lỗi trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    private GroupChat findExistingGroupChat(List<GroupChatUser> testSender, List<Long> receiverIds) {

        for (GroupChatUser senderChatUser : testSender) {
            GroupChat groupChatSender = groupChatRepository.findById(senderChatUser.getId().getGroupId()).orElse(null);
            if (groupChatSender == null) continue;

            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(groupChatSender.getId());
            List<String> groupMemberPhoneNumbers = groupChatUsers.stream()
                    .map(groupChatUser -> groupChatUser.getId().getPhoneNumberUser())
                    .collect(Collectors.toList());

            // Tạo danh sách tất cả các thành viên dự kiến (bao gồm người gửi và người nhận)
            List<String> expectedMemberPhoneNumbers = new ArrayList<>();
            for (Long receiverId : receiverIds) {
                User user = userRepository.findById(receiverId);
                expectedMemberPhoneNumbers.add(user.getPhoneNumber());
            }
            expectedMemberPhoneNumbers.add(senderChatUser.getId().getPhoneNumberUser());

            // Kiểm tra số lượng thành viên và sự tồn tại của tất cả các thành viên trong nhóm
            if (groupMemberPhoneNumbers.size() == expectedMemberPhoneNumbers.size() && groupMemberPhoneNumbers.containsAll(expectedMemberPhoneNumbers)) {
                return groupChatSender;
            }
        }
        return null;
    }

    private GroupChat createNewGroupChat(AddNewRoomRequest addNewRoomRequest) {
        GroupChat groupChat = new GroupChat();
        User firstReceiver = userRepository.findById(addNewRoomRequest.getReceiverIds().get(0));
        if (firstReceiver == null) throw new IllegalArgumentException("Người nhận đầu tiên không tồn tại!");

        if (addNewRoomRequest.getReceiverIds().size() == 1) {
            groupChat.setGroupName(firstReceiver.getFullName());
            groupChat.setAvatar(firstReceiver.getImageAvatarUrl());
        } else {
            if (addNewRoomRequest.getGroupName().isEmpty()) {
                throw new IllegalArgumentException("Tên nhóm không được để trống!");
            } else if (addNewRoomRequest.getGroupAvatarFile().isEmpty() && addNewRoomRequest.getGroupAvatarFile() == null) {
                throw new IllegalArgumentException("Avatar của nhóm không được để trống!");
            } else {
                groupChat.setGroupName(addNewRoomRequest.getGroupName());
                groupChat.setAvatar(fileStorageService.storeFile(addNewRoomRequest.getGroupAvatarFile()));
            }
        }
        return groupChatRepository.save(groupChat);
    }

    private void addUsersToGroupChat(GroupChat groupChat, User sender, List<Long> receiverIds, boolean existingChat) {
        if (!existingChat || !isUserInGroupChat(sender.getPhoneNumber(), groupChat.getId())) {
            GroupChatUser groupChatSender = new GroupChatUser();
            GroupChatUserId groupChatSenderId = new GroupChatUserId();
            groupChatSenderId.setGroupId(groupChat.getId());
            groupChatSenderId.setPhoneNumberUser(sender.getPhoneNumber());
            groupChatSender.setId(groupChatSenderId);
            groupChatSender.setJoinedAt(new Date(new Date().getTime()));
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
                groupChatReceiver.setJoinedAt(new Date(new Date().getTime()));
                groupChatUserRepository.save(groupChatReceiver);
            }
        }
    }

    @Override
    public String addUsersToRoom(Long userId, Long roomId, List<String> usersPhoneNumber) {
        try {
            if (!groupChatRepository.existsById(roomId)) {
                return "Nhóm chat không tồn tại!";
            }

            User user = userRepository.findById(userId);

            for (String phoneNumber : usersPhoneNumber) {
                if (!userRepository.existsUserByPhoneNumber(phoneNumber)) {
                    return "Người dùng cần thêm vào nhóm chat không tồn tại!";
                }
                if (phoneNumber.equals(user.getPhoneNumber())) {
                    return "Bạn đã là thành viên của nhóm chat!";
                }
            }

            GroupChat groupChat = groupChatRepository.findById(roomId).get();
            List<GroupChatUser> groupChatUsersListWithDeletedFalse = groupChatUserRepository.findAllByGroupId(roomId);
            Boolean check = false;
            for (GroupChatUser gcu : groupChatUsersListWithDeletedFalse) {
                if (gcu.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                    check = true;
                }
            }
            if (!check) {
                throw new Exception("Người dùng không có quyền mời người dùng khác vào nhóm chat!");
            }

            List<GroupChatUser> groupChatUserList = groupChatUserRepository.findAllByGroupIdAndGetDeleted(roomId);

            if (!checkValidUsersToAddToRoom(groupChatUserList, usersPhoneNumber)) {
                return "Danh sách người dùng cần thêm vào nhóm chat không hợp lệ!";
            }

            for (String phoneNumber : usersPhoneNumber) {
                User userInGroup = userRepository.findByPhoneNumber(phoneNumber);
                GroupChatUserId groupChatUserId = new GroupChatUserId();
                groupChatUserId.setGroupId(roomId);
                groupChatUserId.setPhoneNumberUser(phoneNumber);
                Optional<GroupChatUser> groupChatUserForUpdate = groupChatUserRepository.findById(groupChatUserId);
                GroupChatUser groupChatUser;
                if (groupChatUserForUpdate.isPresent()) {
                    groupChatUser = groupChatUserForUpdate.get();
                    groupChatUser.setIsDeleted(false);
                } else {
                    groupChatUser = new GroupChatUser();
                    groupChatUser.setId(groupChatUserId);
                    groupChatUser.setJoinedAt(new Date(new Date().getTime()));
                }
                MessageChat messageChat = new MessageChat();
                messageChat.setUser(user);
                messageChat.setSeen(true);
                messageChat.setGroupChat(groupChat);
                messageChat.setIsSystem(true);
                messageChat.setContent(String.format("%s vừa tham gia nhóm", userInGroup.getFullName()));
                messageChat.setSendAt(new Date(new Date().getTime()));
                messageChat.setUpdatedAt(new Date(new Date().getTime()));
                messageChatRepository.save(messageChat);
                groupChatUserRepository.save(groupChatUser);
                List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(groupChat.getId());
                for (GroupChatUser userGroup : groupChatUsers) {
                    notifyToUser(userRepository.findByPhoneNumber(userGroup.getId().getPhoneNumberUser()).getId(), ChatNotification.builder().roomId(messageChat.getGroupChat().getId()).typeNotification(TypeNotification.CREATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                }
            }
            return "Thêm người dùng vào nhóm chat thành công!";
        } catch (Exception e) {
            return "Đã có lỗi xảy ra!";
        }
    }

    private Boolean checkValidUsersToAddToRoom(List<GroupChatUser> groupChatUsers, List<String> usersPhoneNumber) {
        for (GroupChatUser groupChatUser : groupChatUsers) {
            for (String phoneNumber : usersPhoneNumber) {
                if (groupChatUser.getId().getPhoneNumberUser().equals(phoneNumber)) {
                    if (!groupChatUser.getIsDeleted()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isUserInGroupChat(String phoneNumber, Long groupId) {
        List<GroupChatUser> users = groupChatUserRepository.findAllByPhoneNumberUser(phoneNumber);
        return users.stream().anyMatch(user -> user.getId().getGroupId().equals(groupId));
    }

    @Override
    public GetMessages createChatMessage(Long userId, AddNewChatMessageRequest addNewChatMessageRequest) {
        try {
            List<ChatMessageResponse> chatMessageResponses = new ArrayList<>();
            User user = userRepository.findById(userId);
            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(addNewChatMessageRequest.getRoomId());
            if ((addNewChatMessageRequest.getContent() == null || addNewChatMessageRequest.getContent().isEmpty()) && (addNewChatMessageRequest.getFiles() == null || addNewChatMessageRequest.getFiles()[0].isEmpty())) {
                return new GetMessages("Không thể gửi tin nhắn không có nội dung hoặc file!", chatMessageResponses);
            }
            if (groupChatUsers.isEmpty()) {
                return new GetMessages("Mã phòng không tồn tại!", chatMessageResponses);
            }
            Boolean check = false;
            for (GroupChatUser groupChatUser : groupChatUsers) {
                if (groupChatUser.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                return new GetMessages("Người dùng không có quyền gửi tin nhắn trong phòng này!", chatMessageResponses);
            }
            List<String> messageParts;
            if (addNewChatMessageRequest.getContent() != null && addNewChatMessageRequest.getContent().length() > 2330) {
                messageParts = splitMessage(addNewChatMessageRequest.getContent());
            } else {
                messageParts = new ArrayList<>();
                messageParts.add(addNewChatMessageRequest.getContent());
            }
            boolean isFirstPart = true;
            for (String part : messageParts) {
                MessageChat messageChat = new MessageChat();
                messageChat.setUser(user);
                messageChat.setContent(part);
                if (addNewChatMessageRequest.getDisableActions() != null) {
                    messageChat.setDisableActions(addNewChatMessageRequest.getDisableActions());
                }
                if (addNewChatMessageRequest.getDisableReactions() != null) {
                    messageChat.setDisableReactions(addNewChatMessageRequest.getDisableReactions());
                }
                if (addNewChatMessageRequest.getDistributed() != null) {
                    messageChat.setDistributed(true);
                }
                if (addNewChatMessageRequest.getFailure() != null) {
                    messageChat.setFailure(addNewChatMessageRequest.getFailure());
                }
                if (addNewChatMessageRequest.getSystem() != null) {
                    messageChat.setIsSystem(addNewChatMessageRequest.getSystem());
                }
                if (addNewChatMessageRequest.getSaved() != null) {
                    messageChat.setSaved(true);
                }
                if (addNewChatMessageRequest.getSeen() != null) {
                    messageChat.setSeen(false);
                }
                messageChat.setSendAt(new Date(new Date().getTime()));
                messageChat.setUpdatedAt(new Date(new Date().getTime()));

                if (!groupChatRepository.existsById(addNewChatMessageRequest.getRoomId())) {
                    return new GetMessages("Phòng chat này không tồn tại!", chatMessageResponses);
                }
                GroupChat groupChat = groupChatRepository.findById(addNewChatMessageRequest.getRoomId()).get();
                messageChat.setGroupChat(groupChat);

                if (isFirstPart && addNewChatMessageRequest.getReplyMessageId() != null) {
                    Optional<MessageChat> repliedMessageOptional = messageChatRepository.findById(addNewChatMessageRequest.getReplyMessageId());
                    if (repliedMessageOptional.isEmpty()) {
                        return new GetMessages("Tin nhắn được trả lời không tồn tại!", chatMessageResponses);
                    }
                    MessageChat repliedMessage = repliedMessageOptional.get();
                    if (repliedMessage.getGroupChat().getId() != addNewChatMessageRequest.getRoomId()) {
                        return new GetMessages("Tin nhắn được trả lời không tồn tại trong phòng chat hiện tại!", chatMessageResponses);
                    }
                    messageChat.setReplyMessage(repliedMessage);
                }

                List<Resource> resourceList = new ArrayList<>();
                if (addNewChatMessageRequest.getFiles() != null && !addNewChatMessageRequest.getFiles()[0].isEmpty() && isFirstPart) {
                    resourceList = Arrays.stream(addNewChatMessageRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)).collect(Collectors.toList());
                }
                messageChat.setFiles(resourceList);
                if (addNewChatMessageRequest.getTaggedUserId() != null && addNewChatMessageRequest.getTaggedUserId().size() != 0) {
                    List<User> taggedUsers = new ArrayList<>();
                    for (Long tagUserId : addNewChatMessageRequest.getTaggedUserId()) {
                        if (tagUserId == userId) {
                            return new GetMessages("Không thể tự gắn thẻ bản thân!", chatMessageResponses);
                        }
                        List<GroupChatUser> groupChatUserForTest = groupChatUserRepository.findAllByGroupId(addNewChatMessageRequest.getRoomId());
                        if (!userRepository.existsUserById(tagUserId)) {
                            return new GetMessages("Người dùng được gắn thẻ với ID " + tagUserId + " không tồn tại!", chatMessageResponses);
                        }
                        Boolean checkUser = false;
                        User taggedUser = userRepository.findById(tagUserId);
                        for (GroupChatUser groupChatUser : groupChatUserForTest) {
                            if (groupChatUser.getId().getPhoneNumberUser().equals(taggedUser.getPhoneNumber())) {
                                checkUser = true;
                                break;
                            }
                        }
                        if (!checkUser) {
                            return new GetMessages("Người dùng được gắn thẻ với ID " + tagUserId + " không có tham gia phòng chat!", chatMessageResponses);
                        }
                        taggedUsers.add(taggedUser);
                    }
                    messageChat.setTagUsers(taggedUsers);
                }
                messageChat = messageChatRepository.save(messageChat);

                User receiver = new User();
                for (GroupChatUser gcu : groupChatUsers) {
                    if (!gcu.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                        receiver = userRepository.findByPhoneNumber(gcu.getId().getPhoneNumberUser());
                        break;
                    }
                }

                List<MessageChat> messageChatsForUpdate = messageChatRepository.findAllByGroupIdAndUserPhoneNumber(groupChat.getId(), receiver.getPhoneNumber());
                for (MessageChat message : messageChatsForUpdate) {
                    message.setSeen(true);
                    messageChatRepository.save(message);
                }

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
                response.setDistributed(true);
                response.setSeen(messageChat.getSeen());
                response.setDeleted(messageChat.getDeleted());
                response.setFailure(messageChat.getFailure());
                response.setDisableActions(messageChat.getDisableActions());
                response.setDisableReactions(messageChat.getDisableReactions());
                Map<String, List<String>> reactions = new HashMap<>();
                response.setReactions(reactions);

                if (isFirstPart && messageChat.getReplyMessage() != null) {
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
                List<UserOfRoom> taggedUsers = new ArrayList<>();
                if (messageChat.getTagUsers() != null) {
                    for (User taggedUser : messageChat.getTagUsers()) {
                        UserOfRoom getTaggedUser = new UserOfRoom();
                        getTaggedUser.set_id(taggedUser.getId() + "");
                        getTaggedUser.setAvatar("http://localhost:8181/media/getImage/" + taggedUser.getImageAvatarUrl());
                        StatusOfUser status = new StatusOfUser();
                        status.setState(taggedUser.getStatus() + "");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(taggedUser.getLastActive());
                        calendar.add(Calendar.HOUR_OF_DAY, -7);
                        Date adjustedDate = calendar.getTime();
                        status.setLastChanged(formatDateTime(adjustedDate));
                        getTaggedUser.setStatus(status);
                        getTaggedUser.setUsername(taggedUser.getFullName());
                        taggedUsers.add(getTaggedUser);
                    }
                }
                response.setTaggedUser(taggedUsers);
                for (GroupChatUser groupChatUser : groupChatUsers) {
                    if (!groupChatUser.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                        notifyToUser(userRepository.findByPhoneNumber(groupChatUser.getId().getPhoneNumberUser()).getId(), ChatNotification.builder().roomId(addNewChatMessageRequest.getRoomId()).typeNotification(TypeNotification.CREATE).message(response).build());
                        notifyToUser(userId, ChatNotification.builder().roomId(addNewChatMessageRequest.getRoomId()).typeNotification(TypeNotification.CREATE).message(response).build());
                    }
                }
                chatMessageResponses.add(response);

                isFirstPart = false;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            return new GetMessages("Tin nhắn đã được gửi!", chatMessageResponses);
        } catch (Exception e) {
            System.out.println(e.toString());
            List<ChatMessageResponse> chatMessageResponses = new ArrayList<>();
            return new GetMessages("Đã xảy ra lỗi trong quá trình thực hiện, vui lòng thử lại sau!", chatMessageResponses);
        }
    }

    public static List<String> splitMessage(String message) {
        List<String> parts = new ArrayList<>();
        if (message.length() <= 2330) {
            parts.add(message);
            return parts;
        }

        int maxPartLength = 2230;
        int currentIndex = 0;
        while (currentIndex < message.length()) {
            int endIndex = Math.min(currentIndex + maxPartLength, message.length());

            if (endIndex < message.length() && message.charAt(endIndex) != ' ') {
                int lastSpaceIndex = message.lastIndexOf(' ', endIndex);
                if (lastSpaceIndex > currentIndex) {
                    endIndex = lastSpaceIndex;
                }
            }

            String part = message.substring(currentIndex, endIndex).trim();
            parts.add(part);
            currentIndex = endIndex;

            while (currentIndex < message.length() && message.charAt(currentIndex) == ' ') {
                currentIndex++;
            }
        }
        return parts;
    }

    @Override
    public GetAMessage updateChatMessage(Long userId, UpdateChatMessageRequest updateChatMessageRequest) {
        try {
            User user = userRepository.findById(userId);
            Optional<MessageChat> messageChatOptional = messageChatRepository.findById(updateChatMessageRequest.getMessageId());
            if (messageChatOptional.isEmpty()) {
                return new GetAMessage("Tin nhắn không tồn tại!", new ChatMessageResponse());
            }
            if (messageChatOptional.get().getGroupChat().getId() != updateChatMessageRequest.getRoomId()) {
                return new GetAMessage("Thông tin tin nhắn không chính xác", new ChatMessageResponse());
            }
            MessageChat messageChat = messageChatOptional.get();
            if (user.getId() != messageChat.getUser().getId()) {
                if (updateChatMessageRequest.getReactions() != null) {
                    if (updateChatMessageRequest.getSeen() != null) {
                        messageChat.setSeen(updateChatMessageRequest.getSeen());
                    }
                    Map<String, List<String>> reactionsMap = updateChatMessageRequest.getReactionsMap();
                    reactionRepository.deleteByMessageChat(messageChat);
                    List<Reaction> newReactions = new ArrayList<>();
                    for (Map.Entry<String, List<String>> entry : reactionsMap.entrySet()) {
                        String emoji = entry.getKey();
                        for (String userIdStr : entry.getValue()) {
                            Long reactingUserId = Long.valueOf(userIdStr);
                            if (!userRepository.existsUserById(reactingUserId)) {
                                return new GetAMessage("Người dùng không tồn tại!", new ChatMessageResponse());
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

                    List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(updateChatMessageRequest.getRoomId());
                    for (GroupChatUser groupChatUser : groupChatUsers) {
                        if (!groupChatUser.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                            // Thông báo tới người được nhận tin nhắn
                            notifyToUser(userRepository.findByPhoneNumber(groupChatUser.getId().getPhoneNumberUser()).getId(), ChatNotification.builder().roomId(updateChatMessageRequest.getRoomId()).typeNotification(TypeNotification.UPDATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                            notifyToUser(userId, ChatNotification.builder().roomId(updateChatMessageRequest.getRoomId()).typeNotification(TypeNotification.UPDATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                        }
                    }
                    return getAMessageToSend(messageChat);
                }
                return new GetAMessage("Người dùng không có quyền cập nhật tin nhắn này!", new ChatMessageResponse());
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
                messageChat.setDistributed(true);
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
                    System.out.println("Files:");
                    for (MultipartFile file : updateChatMessageRequest.getFiles()) {
                        System.out.println(file);
                    }
                    List<Resource> resourceList = Arrays.stream(updateChatMessageRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)).collect(Collectors.toList());
                    messageChat.setFiles(resourceList);
                }
            }

            if (updateChatMessageRequest.getReplyMessageId() != null) {
                Optional<MessageChat> repliedMessageOptional = messageChatRepository.findById(updateChatMessageRequest.getReplyMessageId());
                if (repliedMessageOptional.isEmpty()) {
                    return new GetAMessage("Tin nhắn được trả lời không tồn tại!", new ChatMessageResponse());
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
                            return new GetAMessage("Người dùng không tồn tại!", new ChatMessageResponse());
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
            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(updateChatMessageRequest.getRoomId());
            for (GroupChatUser groupChatUser : groupChatUsers) {
                if (!groupChatUser.getId().getPhoneNumberUser().equals(user.getPhoneNumber())) {
                    notifyToUser(userRepository.findByPhoneNumber(groupChatUser.getId().getPhoneNumberUser()).getId(), ChatNotification.builder().roomId(updateChatMessageRequest.getRoomId()).typeNotification(TypeNotification.UPDATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                    notifyToUser(userId, ChatNotification.builder().roomId(updateChatMessageRequest.getRoomId()).typeNotification(TypeNotification.UPDATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                }
            }
            return getAMessageToSend(messageChat);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new GetAMessage("Đã xảy ra lỗi trong quá trình thực thi, vui lòng kiểm tra lại!", new ChatMessageResponse());
        }
    }

    @Override
    public GetAMessage getAMessageToSend(MessageChat messageChat) {
        MessageChat updatedMessage = messageChatRepository.save(messageChat);
        ChatMessageResponse response = new ChatMessageResponse();
        response.set_id(updatedMessage.getId() + "");
        response.setContent(updatedMessage.getContent());
        User sender = userRepository.findById(updatedMessage.getUser().getId());
        response.setSenderId(sender.getId() + "");
        response.setUsername(sender.getFullName());
        response.setAvatar("http://localhost:8181/media/getImage/" + sender.getImageAvatarUrl());
        response.setDate(formatDate(updatedMessage.getSendAt()));
        response.setTimestamp(extractTime(updatedMessage.getSendAt()));

        response.setSystem(updatedMessage.getIsSystem());
        response.setSaved(updatedMessage.getSaved());
        response.setDistributed(true);
        response.setSeen(updatedMessage.getSeen());
        response.setDeleted(updatedMessage.getDeleted());
        response.setFailure(updatedMessage.getFailure());
        response.setDisableActions(updatedMessage.getDisableActions());
        response.setDisableReactions(updatedMessage.getDisableReactions());

        MessageChat messageChatForGetReactions = messageChatRepository.findByIdWithReactions(updatedMessage.getId());
        Map<String, List<String>> reactions = new HashMap<>();
        if (messageChatForGetReactions.getReactions() != null) {
            reactions = messageChatForGetReactions.getReactions().stream()
                    .collect(Collectors.groupingBy(
                            Reaction::getEmoji,
                            Collectors.mapping(reaction -> reaction.getUser().getId().toString(), Collectors.toList())
                    ));
        }
        response.setReactions(reactions);
        if (updatedMessage.getReplyMessage() == null) {
            response.setReplyMessage(null);
        } else {
            MessageChat messageChatForReplyMessage = messageChatRepository.findById(updatedMessage.getReplyMessage().getId()).get();
            ReplyMessageResponse replyMessageResponse = new ReplyMessageResponse();
            replyMessageResponse.setContent(messageChatForReplyMessage.getContent());
            replyMessageResponse.setSenderId(messageChatForReplyMessage.getUser().getId() + "");
            List<FileData> files = new ArrayList<>();
            if (messageChatForReplyMessage.getFiles() != null) {
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
            }
            replyMessageResponse.setFiles(files);
            response.setReplyMessage(replyMessageResponse);
        }

        List<FileData> files = new ArrayList<>();
        if (updatedMessage.getFiles() != null) {
            for (Resource resource : updatedMessage.getFiles()) {
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
        }
        response.setFiles(files);

        List<UserOfRoom> taggedUsers = new ArrayList<>();
        if (updatedMessage.getTagUsers() != null) {
            for (User taggedUser : updatedMessage.getTagUsers()) {
                UserOfRoom getTaggedUser = new UserOfRoom();
                getTaggedUser.set_id(taggedUser.getId() + "");
                getTaggedUser.setAvatar("http://localhost:8181/media/getImage/" + taggedUser.getImageAvatarUrl());
                StatusOfUser status = new StatusOfUser();
                status.setState(taggedUser.getStatus() + "");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(taggedUser.getLastActive());
                calendar.add(Calendar.HOUR_OF_DAY, -7);
                Date adjustedDate = calendar.getTime();
                status.setLastChanged(formatDateTime(adjustedDate));
                getTaggedUser.setStatus(status);
                getTaggedUser.setUsername(taggedUser.getFullName());
                taggedUsers.add(getTaggedUser);
            }
        }
        response.setTaggedUser(taggedUsers);
        if (updatedMessage.getDeleted()) {
            response.setContent("⛔ This message was deleted");
        } else if ((updatedMessage.getContent().isEmpty() || updatedMessage.getContent() == null) && updatedMessage.getFiles() != null && !updatedMessage.getFiles().isEmpty()) {
            response.setContent("\uD83D\uDCF9 Media content");
        }
        return new GetAMessage("Cập nhật tin nhắn thành công!", response);
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
            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(messageChat.getGroupChat().getId());
            for (GroupChatUser groupChatUser : groupChatUsers) {
                if (!groupChatUser.getId().getPhoneNumberUser().equals(messageChat.getUser().getPhoneNumber())) {
                    notifyToUser(userRepository.findByPhoneNumber(groupChatUser.getId().getPhoneNumberUser()).getId(), ChatNotification.builder().roomId(messageChat.getGroupChat().getId()).typeNotification(TypeNotification.UPDATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                    notifyToUser(userId, ChatNotification.builder().roomId(messageChat.getGroupChat().getId()).typeNotification(TypeNotification.UPDATE).message(getAMessageToSend(messageChat).getChatMessageResponse()).build());
                }
            }
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

    @Override
    public String getUserPhoneNumber(Long userId, Long roomId) {
        List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(roomId);

        User userForCheck = userRepository.findById(userId);
        for (GroupChatUser groupChatUser : groupChatUsers) {
            if (!userForCheck.getPhoneNumber().equals(groupChatUser.getId().getPhoneNumberUser())) {
                return groupChatUser.getId().getPhoneNumberUser();
            }

        }
        return null;
    }

    @Override
    public void notifyToUser(Long userId, ChatNotification chatNotification) {
        try {
            GetAllRooms getAllRoomResponse = getAllRooms(userId, "desc");
            chatNotification.setRoomInfo(getAllRoomResponse.getAllRoomResponses.stream().filter(room -> Long.valueOf(room.getRoomId()).equals(chatNotification.getRoomId())).toList().get(0));
            simpMessagingTemplate.convertAndSendToUser("user" + userId, "/topic/specific-user", chatNotification);
        } catch (MessagingException exception) {
            System.out.println("Have some error");
            System.out.println(exception.toString());
        } catch (JsonProcessingException e) {
            System.out.println("Have some error");
            System.out.println(e.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public class GetAMessage {
        private String message;
        private ChatMessageResponse chatMessageResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetMessages {
        private String message;
        private List<ChatMessageResponse> chatMessageResponses;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllRooms {
        private String message;
        private List<GetAllRoomResponse> getAllRoomResponses;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetARoomInfo {
        private String message;
        private GetRoomInfo getRoomInfo;
    }


    @Override
    public String sendNotificationDeclined(Long userId, SendNotificationRequest sendNotificationRequest) {
        try {
            Optional<GroupChat> groupChatOptional = groupChatRepository.findById(sendNotificationRequest.getRoomId());
            if (groupChatOptional.isEmpty()) {
                return "Phòng cần gửi thông báo không tồn tại!";
            }

            User receiver = userRepository.findById(sendNotificationRequest.getReceiverId());
            if (receiver == null) {
                return "Người dùng cần gửi thông báo không tồn tại!";
            }

            User sender = userRepository.findById(userId);

            List<GroupChatUser> groupChatUserList = groupChatUserRepository.findAllByGroupId(sendNotificationRequest.getRoomId());
            Boolean checkReceiverInRoom = false;
            Boolean checkSenderInRoom = false;
            for (GroupChatUser groupChatUser : groupChatUserList) {
                if (groupChatUser.getId().getPhoneNumberUser().equals(receiver.getPhoneNumber())) {
                    checkReceiverInRoom = true;
                }
                if (groupChatUser.getId().getPhoneNumberUser().equals(sender.getPhoneNumber())) {
                    checkSenderInRoom = true;
                }
            }

            if (!checkSenderInRoom) {
                return "Bạn không có quyền gửi thông báo!";
            }

            if (!checkReceiverInRoom) {
                return "Người cần gửi thông báo không thuộc nhóm!";
            }

            notifyToUser(receiver.getId(), ChatNotification.builder().roomId(sendNotificationRequest.getRoomId()).typeNotification(TypeNotification.UPDATE).message(sendNotificationRequest.getMessage()).build());

            return "Gửi thông báo thành công!";
        } catch (Exception e) {
            return "Gửi thông báo thất bại!";
        }

    }
}
