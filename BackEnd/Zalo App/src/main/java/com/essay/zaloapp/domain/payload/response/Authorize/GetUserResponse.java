package com.essay.zaloapp.domain.payload.response.Authorize;

import com.essay.zaloapp.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {
    private Long id = null;

    private String phoneNumber = null;

    private Date birthDay = null;

    private Sex sex = null;

    private String imageAvatarUrl = null;

    private String imageCoverPhotoUrl = null;

    private Date lastActive = null;

    private Boolean isLocked = null;

    private Boolean isConfirmed = null;

    private String fullName = null;
}
