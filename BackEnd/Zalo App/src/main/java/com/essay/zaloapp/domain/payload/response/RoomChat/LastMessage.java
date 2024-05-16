package com.essay.zaloapp.domain.payload.response.RoomChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LastMessage {
    private String _id;
    private String content;
    private String senderId;
    private String username;
    private String timestamp;
    private Boolean saved;
    private Boolean distributed;
    private Boolean seen;
    private Boolean isNew;
}
