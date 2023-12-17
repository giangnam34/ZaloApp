package com.essay.zaloapp.domain.payload.response.Authorize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoUser {

    private String userName;

    private String imageAvatar;

    private String phoneNumber;
}
