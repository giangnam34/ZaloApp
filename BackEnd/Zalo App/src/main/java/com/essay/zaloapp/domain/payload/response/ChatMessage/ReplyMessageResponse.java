package com.essay.zaloapp.domain.payload.response.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyMessageResponse {
    private String content;
    private String senderId;
    private List<FileData> files;
}
