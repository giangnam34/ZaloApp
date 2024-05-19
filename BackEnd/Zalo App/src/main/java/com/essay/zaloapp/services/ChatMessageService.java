package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.response.ChatMessage.ChatMessageResponse;
import com.essay.zaloapp.services.impl.ChatMessageServiceImpl;

import java.util.List;

public interface ChatMessageService {


    ChatMessageServiceImpl.GetAllMessages getAllMessages(Long roomId, Long userId, int page, int size) throws Exception;

    ChatMessageServiceImpl.GetAllRooms getAllRooms(Long userId, String sortOrder) throws Exception;

    String deleteRoom(Long userId, Long roomId);

    String createRoom(Long senderId, Long receiverId);
}
