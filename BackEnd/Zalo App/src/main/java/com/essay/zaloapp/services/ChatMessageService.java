package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewChatMessageRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.UpdateChatMessageRequest;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatMessageResponse;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatNotification;
import com.essay.zaloapp.services.impl.ChatMessageServiceImpl;

import java.util.List;

public interface ChatMessageService {


    ChatMessageServiceImpl.GetAllMessages getAllMessages(Long roomId, Long userId, int page, int size) throws Exception;

    ChatMessageServiceImpl.GetAllRooms getAllRooms(Long userId, String sortOrder) throws Exception;

    ChatMessageServiceImpl.GetARoomInfo getRoomInfo(Long userId, Long roomId) throws Exception;

    String deleteRoom(Long userId, Long roomId);

    String createRoom(Long senderId, List<Long> receiverIds);


    String addUsersToRoom(Long userId, Long roomId, List<String> usersPhoneNumber);

    ChatMessageServiceImpl.GetAMessage createChatMessage(Long userId, AddNewChatMessageRequest addNewChatMessageRequest);


    ChatMessageServiceImpl.GetAMessage updateChatMessage(Long userId, UpdateChatMessageRequest updateChatMessageRequest);

    String deleteMessage(Long userId, Long messageId);

    String getUserPhoneNumber(Long userId, Long roomId);

    void notifyToUser(Long userId, ChatNotification chatNotification);
}
