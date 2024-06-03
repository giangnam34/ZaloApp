package com.essay.zaloapp.domain.payload.response.ChatMessage;

import com.essay.zaloapp.services.impl.ChatMessageServiceImpl;
import lombok.*;
import org.springframework.http.ContentDisposition;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private Long roomId;

    // Update or have new message
    private String typeNotification;

    private ChatMessageResponse message;
}
