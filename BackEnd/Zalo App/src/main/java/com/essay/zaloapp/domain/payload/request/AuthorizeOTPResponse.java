package com.essay.zaloapp.domain.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthorizeOTPResponse {
    public String phoneNumber;
    public String otpCode;
}
