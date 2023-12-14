package com.essay.zaloapp.domain.payload.request.Friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {

    // Người gửi
    private String fromPhoneNumberUser;

    // Người nhận
    private String toPhoneNumberUser;
}
