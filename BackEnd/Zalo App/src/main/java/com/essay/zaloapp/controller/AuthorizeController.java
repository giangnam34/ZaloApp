package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.models.OTPCode;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.*;
import com.essay.zaloapp.domain.payload.response.ResultSMSResponse;
import com.essay.zaloapp.domain.payload.response.SignUpResponse;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/v1/auth")
public class AuthorizeController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) throws Exception {
        try {
            return authenticationService.signUp(signUpRequest);
        }catch (Exception ex){
            System.out.println(ex.toString());
            return ResponseEntity.badRequest().body(new SignUpResponse("Có lỗi trong quá trình gửi OTP. Vui lòng thử lại"));
        }
    }

    @PostMapping("/OTP")
    public ResponseEntity<?> authorizeOTP(@RequestBody AuthorizeOTPResponse authorizeOTPResponse){
        return authenticationService.authorizeOTP(authorizeOTPResponse);
    }

    @PostMapping("/send-OTP/{phoneNumber}")
    public ResponseEntity<?> sendOTP(@PathVariable String phoneNumber) throws IOException, InterruptedException {
        if (!userRepository.existsUserByPhoneNumber(phoneNumber))
            return ResponseEntity.badRequest().body("Người dùng chưa đăng ký tài khoản");
        String otpCode = authenticationService.generateOTPCode(100000,999999);
        ResultSMSResponse result = new ObjectMapper().readValue(authenticationService.sendOTP(phoneNumber,otpCode), ResultSMSResponse.class);
        Long statusCode = authenticationService.getStatusSendOTP(result);
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (statusCode == 2) return ResponseEntity.badRequest().body(new SignUpResponse("Hmm Có lỗi trong quá trình gửi OTP. Vui lòng thử lại"));
        OTPCode otpCode1 = user.getOtpCode();
        if (otpCode1 == null) {
            otpCode1 = new OTPCode();
            otpCode1.setUser(user);
        }
        otpCode1.setValue(otpCode);
        otpCode1.setExpireTime(new Date(new Date().getTime() + 7*60*60000 + 4*60000));
        user.setOtpCode(otpCode1);
        userRepository.save(user);
        if (statusCode == 0 || statusCode == -1) return ResponseEntity.ok(new SignUpResponse("Hệ thống đang gửi mã OTP. Xin vui lòng chờ trong ít phút"));
        return ResponseEntity.ok(new SignUpResponse("Hệ thống đã gửi mã OTP tới số điện thoại. Xin vui lòng kiểm tra và nhập mã"));
    }

    @PutMapping ("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        return authenticationService.forgetPassword(forgetPasswordRequest);
    }
}
