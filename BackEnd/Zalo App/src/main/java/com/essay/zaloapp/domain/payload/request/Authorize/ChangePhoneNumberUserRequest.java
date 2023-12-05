package com.essay.zaloapp.domain.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneNumberUserRequest {

    private String newPhoneNumber;
}
