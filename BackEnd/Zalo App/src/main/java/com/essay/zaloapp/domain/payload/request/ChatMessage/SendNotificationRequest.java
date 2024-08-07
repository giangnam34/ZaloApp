package com.essay.zaloapp.domain.payload.request.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendNotificationRequest {
    private Long receiverId;
    private Long roomId;
    private String message;
}
