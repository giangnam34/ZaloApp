package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewChatMessageRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.UpdateChatMessageRequest;
import com.essay.zaloapp.secruity.StompPrincipal;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.ChatMessageService;
import com.essay.zaloapp.services.ChatService;
import com.essay.zaloapp.services.impl.ChatMessageServiceImpl;
import com.essay.zaloapp.services.impl.SocialMediaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/chat/")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public void addUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            System.out.println(userDetails.getUsername());
            chatService.connect(userDetails.getUsername());
        }
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public void disconnect() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
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

    @PostMapping("/create-room")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody List<Long> receiverIds) {
        String result = chatMessageService.createRoom(userPrincipal.getId(), receiverIds);
        return result.equals("Tạo cuộc hội thoại thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
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
        ChatMessageServiceImpl.GetAMessage result = chatMessageService.createChatMessage(userPrincipal.getId(), addNewChatMessageRequest);
        return result.getMessage().equals("Tin nhắn đã được gửi!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PutMapping("/update-message")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute UpdateChatMessageRequest updateChatMessageRequest) {
//        System.out.println(updateChatMessageRequest.toString());
//        return null;
        String result = chatMessageService.updateChatMessage(userPrincipal.getId(), updateChatMessageRequest);
        System.out.println(result);
        return result.equals("Cập nhật tin nhắn thành công!") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @MessageMapping("/hello")
    @SendToUser("/topic/hello")
    public void handleHello(SimpMessageHeaderAccessor sha, Map<String, Object> message) {
        Principal principal = sha.getUser();
        if (principal != null) {
            String username = principal.getName();
            // Process the message
        }
    }
}
