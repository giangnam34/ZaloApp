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
public class GetRoomInfo {
    private String roomId;
    private String roomName;
    private String roomAvatar;
    private int membersCount;
    private List<UserOfRoom> users;
}
