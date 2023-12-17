package com.essay.zaloapp.domain.payload.request.Authorize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
    @NotNull
    public String oldPassword;
    @NotNull
    public String newPassword;
    @NotNull
    public String reEnterNewPassword;
}
