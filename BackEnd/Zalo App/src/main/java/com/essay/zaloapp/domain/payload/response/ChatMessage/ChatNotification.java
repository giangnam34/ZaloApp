package com.essay.zaloapp.domain.payload.response.ChatMessage;

import lombok.*;
import org.springframework.http.ContentDisposition;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private String id;
    private String senderPhoneNumber;
    private String recipientPhoneNumber;
    private ChatMessageResponse chatMessageResponse;
}
