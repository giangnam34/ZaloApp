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
public class AddNewRoomRequest {
    private String groupName;
    private MultipartFile groupAvatarFile;
    private List<Long> receiverIds;
}
