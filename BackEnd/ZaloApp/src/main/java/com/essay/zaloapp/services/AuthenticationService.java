package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.request.LoginRequest;
import com.essay.zaloapp.domain.payload.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> signUp(SignUpRequest signUpRequest);

    boolean isValidPassword(String password);

    String sendOTP(String toPhoneNumber);

    String generateOTPCode(int minRange, int maxRange);

    String formatPhoneNumbertoE164(String phoneNumber);
}
