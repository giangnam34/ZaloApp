package com.essay.zaloapp.domain.payload.request.Authorize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneNumberUserRequest {

    private String newPhoneNumber;
}
