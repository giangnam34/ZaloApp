package com.essay.zaloapp.domain.payload.request.Authorize;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthorizeOTPRequest {
    public String phoneNumber;
    public String otpCode;
}
