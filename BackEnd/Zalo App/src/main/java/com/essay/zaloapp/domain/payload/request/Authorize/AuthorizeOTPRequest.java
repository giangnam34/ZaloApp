package com.essay.zaloapp.domain.payload.request;

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
