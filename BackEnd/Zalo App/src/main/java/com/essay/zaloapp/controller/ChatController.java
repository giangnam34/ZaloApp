package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.enums.TypeNotification;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.ChatMessage.*;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatNotification;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.ChatMessageService;
import com.essay.zaloapp.services.ChatService;
import com.essay.zaloapp.services.impl.ChatMessageServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/chat/")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String sendToGroup(Message message) throws Exception {
        System.out.println("There have a message");
        System.out.println(message.getPayload());
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return "Test";
    }

    @MessageMapping("/room")
    public String sendRTCConnectionToSpecificUser(Principal user, @Header("userId") String userId, @Header("userSend") String userSend, RTCConnection message) throws Exception {
        System.out.println("There have a message to specific user");
        System.out.println("UserId " + userId);
        System.out.println(message.getEvent());
        ChatNotification chatNotification = ChatNotification.builder().roomId(1L).typeNotification(TypeNotification.RTC_CONNECTION).message(message).build();
        try {
            HashMap<String,Object> header = new HashMap<>();
            header.put("userId", userId.substring(userId.indexOf("user")+4));
            header.put("userSend", userSend.substring(userId.indexOf("user")+4));
            simpMessagingTemplate.convertAndSendToUser(userId ,"/topic/specific-user", chatNotification,header);
        } catch (MessagingException exception){
            System.out.println("Have some error");
            System.out.println(exception);
        }
        return "Test " + user.getName();
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public void addUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            System.out.println(userDetails.getUsername());
            chatService.connect(userDetails.getUsername());
        }
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public void disconnect() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            System.out.println(userDetails.getUsername());
            chatService.disconnect(userDetails.getUsername());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllConnectedUsers() {
        return ResponseEntity.ok(chatService.findConnectedUsers());
    }

    @GetMapping("/get-messages/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long roomId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) throws Exception {
        ChatMessageServiceImpl.GetAllMessages result = chatMessageService.getAllMessages(roomId, userPrincipal.getId(), page, size);
        return result.getMessage().equals("Thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/get-rooms")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllRooms(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(required = false, defaultValue = "asc") String sortOrder) throws Exception {
        ChatMessageServiceImpl.GetAllRooms result = chatMessageService.getAllRooms(userPrincipal.getId(), sortOrder);
        return result.getMessage().equals("Thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/delete-room/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long roomId) {
        String result = chatMessageService.deleteRoom(userPrincipal.getId(), roomId);
        return result.equals("Xóa hội thoại thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PostMapping(value = "/create-room", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestPart("groupName") String groupName,
                                        @RequestPart("groupAvatarFile") MultipartFile groupAvatarFile,
                                        @RequestPart("receiversPhoneNumber") String receiversPhoneNumberJson) {
        try {
            List<String> receiversPhoneNumber = objectMapper.readValue(receiversPhoneNumberJson, new TypeReference<List<String>>() {});

            List<Long> ids = chatMessageService.getListIdsByListPhoneNumber(receiversPhoneNumber);

            AddNewRoomRequest addNewRoomRequest = new AddNewRoomRequest(groupName, groupAvatarFile, ids);

            String result = chatMessageService.createRoom(userPrincipal.getId(), addNewRoomRequest);
            return result.equals("Tạo nhóm thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gửi format data không đúng!");
        }
    }

    @PostMapping("/add-users-to-room/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addUsersToRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long roomId, @RequestBody List<String> usersPhoneNumber) {
        String result = chatMessageService.addUsersToRoom(userPrincipal.getId(), roomId, usersPhoneNumber);
        return result.equals("Thêm người dùng vào nhóm chat thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/delete-message/{messageId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long messageId) {
        String result = chatMessageService.deleteMessage(userPrincipal.getId(), messageId);
        return result.equals("Xóa tin nhắn thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/create-message")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute AddNewChatMessageRequest addNewChatMessageRequest) {
        System.out.println(addNewChatMessageRequest.toString());
        ChatMessageServiceImpl.GetMessages result = chatMessageService.createChatMessage(userPrincipal.getId(), addNewChatMessageRequest);
        if (result.getMessage().equals("Tin nhắn đã được gửi!")) {
            //sendChatNotification(userPrincipal, result);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/update-message")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute UpdateChatMessageRequest updateChatMessageRequest) {
        ChatMessageServiceImpl.GetAMessage result = chatMessageService.updateChatMessage(userPrincipal.getId(), updateChatMessageRequest);
        return result.getMessage().equals("Cập nhật tin nhắn thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

//    @MessageMapping("/chat")
//    @PreAuthorize("hasRole('USER')")
//    public void processMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, AddNewChatMessageRequest addNewChatMessageRequest) {
//        ChatMessageServiceImpl.GetAMessage result = chatMessageService.createChatMessage(userPrincipal.getId(), addNewChatMessageRequest);
//        String receiverPhoneNumber = chatMessageService.getUserPhoneNumber(userPrincipal.getId(), Long.valueOf(result.getChatMessageResponse().get_id()));
//        simpMessagingTemplate.convertAndSendToUser(
//                receiverPhoneNumber,
//                "/queue/messages",
//                ChatNotification.builder()
//                        .id(result.getChatMessageResponse().get_id())
//                        .senderPhoneNumber(userPrincipal.getPhoneNumber())
//                        .recipientPhoneNumber(receiverPhoneNumber)
//                        .chatMessageResponse(result.getChatMessageResponse())
//                        .build()
//        );
//    }

    //    private void sendChatNotification(UserPrincipal userPrincipal, ChatMessageServiceImpl.GetAMessage result) {
//        String receiverPhoneNumber = chatMessageService.getUserPhoneNumber(userPrincipal.getId(), Long.valueOf(result.getChatMessageResponse().get_id()));
//        simpMessagingTemplate.convertAndSendToUser(
//                receiverPhoneNumber,
//                "/queue/messages",
//                ChatNotification.builder()
//                        .id(result.getChatMessageResponse().get_id())
//                        .senderPhoneNumber(userPrincipal.getPhoneNumber())
//                        .recipientPhoneNumber(receiverPhoneNumber)
//                        .chatMessageResponse(result.getChatMessageResponse())
//                        .build()
//        );
//    }
    @GetMapping("/get-room-info/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getRoomInfo(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long roomId) throws Exception {
        ChatMessageServiceImpl.GetARoomInfo result = chatMessageService.getRoomInfo(userPrincipal.getId(), roomId);
        return result.getMessage().equals("Lấy thông tin phòng thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/send-notification-declined")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> sendNotificationDeclined(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute SendNotificationRequest sendNotificationRequest){
        String result = chatMessageService.sendNotificationDeclined(userPrincipal.getId(), sendNotificationRequest);
        return result.equals("Gửi thông báo thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }
}
