package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.ChatMessageService;
import com.essay.zaloapp.services.ChatService;
import com.essay.zaloapp.services.impl.ChatMessageServiceImpl;
import com.essay.zaloapp.services.impl.SocialMediaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
