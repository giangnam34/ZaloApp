package com.essay.zaloapp.domain.payload.request.Authorize;

import com.essay.zaloapp.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeInfoUserRequest {

    private String fullName;

    private Sex sex;

    private Date birthday;
}
