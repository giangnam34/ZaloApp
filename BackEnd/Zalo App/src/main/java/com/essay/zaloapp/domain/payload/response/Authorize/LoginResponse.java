package com.essay.zaloapp.domain.payload.response.Authorize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String jwt;

    public LoginResponse(String jwt){
        this.jwt = "Bearer ".concat(jwt);
    }
}
