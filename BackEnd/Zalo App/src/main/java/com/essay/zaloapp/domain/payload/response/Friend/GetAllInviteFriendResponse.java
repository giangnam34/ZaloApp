package com.essay.zaloapp.domain.payload.response.Friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllInviteFriendResponse {

    private String userName;

    private String imageAvatar;

    private String phoneNumber;

    // false nếu là lời mời người khác gửi, true nếu là lời mời của mình gửi
    private Boolean isInviteFriendFromUser;
}
