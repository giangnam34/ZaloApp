package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.models.User;

import java.util.List;

public interface ChatService {

    void connect(String phoneNumber);


    void disconnect(String phoneNumber);

    List<User> findConnectedUsers();

    Long getChatRoomId(String senderPhoneNumber,
                       String receiverPhoneNumber);

    Long createChatRoom(String senderPhoneNumber, String receiverPhoneNumber);
}
