package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.models.MessageChat;
import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewChatMessageRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.AddNewRoomRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.SendNotificationRequest;
import com.essay.zaloapp.domain.payload.request.ChatMessage.UpdateChatMessageRequest;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatMessageResponse;
import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatNotification;
import com.essay.zaloapp.services.impl.ChatMessageServiceImpl;

import java.util.List;

public interface ChatMessageService {


    ChatMessageServiceImpl.GetAllMessages getAllMessages(Long roomId, Long userId, int page, int size) throws Exception;

    ChatMessageServiceImpl.GetAllRooms getAllRooms(Long userId, String sortOrder) throws Exception;

    List<Long> getListIdsByListPhoneNumber(List<String> usersPhoneNumber) throws Exception;

    ChatMessageServiceImpl.GetARoomInfo getRoomInfo(Long userId, Long roomId) throws Exception;

    String deleteRoom(Long userId, Long roomId);

    String createRoom(Long senderId, AddNewRoomRequest addNewRoomRequest);

    String addUsersToRoom(Long userId, Long roomId, List<String> usersPhoneNumber);


    ChatMessageServiceImpl.GetMessages createChatMessage(Long userId, AddNewChatMessageRequest addNewChatMessageRequest);

    ChatMessageServiceImpl.GetAMessage updateChatMessage(Long userId, UpdateChatMessageRequest updateChatMessageRequest);

    ChatMessageServiceImpl.GetAMessage getAMessageToSend(MessageChat messageChat);

    String deleteMessage(Long userId, Long messageId);

    String getUserPhoneNumber(Long userId, Long roomId);

    void notifyToUser(Long userId, ChatNotification chatNotification);

    String sendNotificationDeclined(Long userId, SendNotificationRequest sendNotificationRequest);
}
