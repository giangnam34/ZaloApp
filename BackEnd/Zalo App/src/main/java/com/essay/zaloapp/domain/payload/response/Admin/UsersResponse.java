package com.essay.zaloapp.domain.payload.response.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private Long id = null;
    private String fullName = null;
    private String phoneNumber = null;
    private String birthDay = null;
    private Long postsCount = null;
    private Long friendsCount = null;
    private Long groupsCount = null;
    private Boolean isLock = false;
    private Boolean isConfirm = false;
}
