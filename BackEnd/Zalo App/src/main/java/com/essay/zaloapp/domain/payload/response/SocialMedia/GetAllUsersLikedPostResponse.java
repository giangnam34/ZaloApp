package com.essay.zaloapp.domain.payload.response.SocialMedia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetAllUsersLikedPostResponse {
    private String phoneNumber;
    private String userName;
    private String imageAvatar;
}
