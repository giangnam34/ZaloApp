package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.request.AuthorizeOTPResponse;
import com.essay.zaloapp.domain.payload.request.LoginRequest;
import com.essay.zaloapp.domain.payload.request.SignUpRequest;
import com.essay.zaloapp.domain.payload.response.ResultSMSResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthenticationService {
    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> signUp(SignUpRequest signUpRequest) throws Exception;

    String sendOTP(String toPhoneNumber, String valueOTP);

    Long getStatusSendOTP(ResultSMSResponse result) throws InterruptedException, IOException;

    boolean isValidPassword(String password);

    String generateOTPCode(int minRange, int maxRange);

    String formatPhoneNumbertoE164(String phoneNumber);

    ResponseEntity<?> authorizeOTP(AuthorizeOTPResponse authorizeOTPResponse);
}
