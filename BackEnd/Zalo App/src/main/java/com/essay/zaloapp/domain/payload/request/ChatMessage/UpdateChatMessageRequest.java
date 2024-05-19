package com.essay.zaloapp.domain.payload.request.ChatMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateChatMessageRequest {
    private Long roomId;
    private Long messageId;
    private String content;
    private Boolean system;
    private Boolean saved;
    private Boolean distributed;
    private Boolean seen;
    private Boolean failure;
    private Boolean disableActions;
    private Boolean disableReactions;
    private MultipartFile[] files;
    private String reactions;
    private Long replyMessageId;

    public Map<String, List<String>> getReactionsMap() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reactions, new TypeReference<Map<String, List<String>>>() {});
    }
}
