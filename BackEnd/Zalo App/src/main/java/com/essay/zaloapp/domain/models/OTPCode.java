package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.OTP;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OTPCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User user;

    @Enumerated(EnumType.STRING)
    private OTP type;

    private String value;

    private Date expireTime;

    public OTPCode(User user, OTP type, String value, Date expireTime){
        this.user = user;
        this.type = type;
        this.value = value;
        this.expireTime = expireTime;
    }
}
