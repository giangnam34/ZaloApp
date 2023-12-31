package com.essay.zaloapp.domain.payload.request.Authorize;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ForgetPasswordRequest {
    private String phoneNumber;
    private String otpCode;
    private String newPassword;
    private String reEnterPassword;
}
