package com.essay.zaloapp.domain.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class findUserByPhoneNumberResponse {

    private String userName;

    private String imageAvatar;

    private String phoneNumber;
}
