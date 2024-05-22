package com.essay.zaloapp.domain.payload.request.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddNewChatMessageRequest {
    private Long roomId;
    private String content;
    private Boolean system;
    private Boolean saved;
    private Boolean distributed;
    private Boolean seen;
    private Boolean failure;
    private Boolean disableActions;
    private Boolean disableReactions;
    private MultipartFile[] files;
    private Long replyMessageId;
    private List<Long> taggedUserId;
}
