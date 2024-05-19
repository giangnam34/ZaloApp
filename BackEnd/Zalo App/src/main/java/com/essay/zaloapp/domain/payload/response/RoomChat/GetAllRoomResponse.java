package com.essay.zaloapp.domain.payload.response.RoomChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetAllRoomResponse {
    private String roomId;
    private String roomName;
    private String avatar;
    private int unreadCount;
    private Long index = null;
    private LastMessage lastMessage;
    private List<UserOfRoom> users;
    private List<String> typingUsers;

}
