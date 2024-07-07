package com.essay.zaloapp.domain.payload.response.ChatMessage;

import com.essay.zaloapp.domain.enums.TypeNotification;
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
    private TypeNotification typeNotification;

    private Object message;

    private Object roomInfo;
}
