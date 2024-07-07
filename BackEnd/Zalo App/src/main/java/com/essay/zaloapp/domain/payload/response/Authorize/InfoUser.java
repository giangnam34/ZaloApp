package com.essay.zaloapp.domain.payload.response.Authorize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoUser {

    private String userName;

    private String imageAvatar;

    private String phoneNumber;

    private String status;

}
