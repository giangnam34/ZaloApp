package com.essay.zaloapp.domain.payload.response.ChatMessage;

import com.essay.zaloapp.domain.payload.response.RoomChat.UserOfRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageResponse {
    private String _id;
    private Long indexId = null;
    private String content;
    private String senderId;
    private String username;
    private String avatar;
    private String date;
    private String timestamp;
    private Boolean system;
    private Boolean saved;
    private Boolean distributed;
    private Boolean seen;
    private Boolean deleted;
    private Boolean failure;
    private Boolean disableActions;
    private Boolean disableReactions;
    private List<FileData> files;
    private Map<String, List<String>> reactions;
    private ReplyMessageResponse replyMessage;
    private List<UserOfRoom> taggedUser;
    private Boolean isBlock;
}
