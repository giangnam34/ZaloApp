package com.essay.zaloapp.domain.payload.response.Authorize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailInfoUser {
    private String userName;

    private String imageAvatar;

    private String imageCoverAvatar;

    private String phoneNumber;

    private String gender;

    private Date birthDay;
}
