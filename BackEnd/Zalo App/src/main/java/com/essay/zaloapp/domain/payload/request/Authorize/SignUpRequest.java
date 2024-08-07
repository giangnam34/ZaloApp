package com.essay.zaloapp.domain.payload.request.Authorize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpRequest {

    @NotNull
    @Length(min = 10, max = 10)
    private String phoneNumber;
    @NotNull
    private String password;
    @NotNull
    private String reEnterPassword;
    @NotNull
    private String fullName;
    @NotNull
    private Date birthDay;
}
