package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.LoginRequest;
import com.essay.zaloapp.domain.payload.request.SignUpRequest;
import com.essay.zaloapp.domain.payload.response.LoginResponse;
import com.essay.zaloapp.domain.payload.response.SignUpResponse;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.secruity.CustomUserDetailsService;
import com.essay.zaloapp.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()
                )
        );

        System.out.println("Authentication: " + authentication.toString());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new LoginResponse(tokenProvider.createToken(authentication)));
    }

    @Override
    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) {
        if (!signUpRequest.getPassword().equals(signUpRequest.getReEnterPassword())) return ResponseEntity.badRequest().body(new SignUpResponse("Mật khẩu không trùng với Xác nhận mật khẩu!"));
        if (userRepository.existsUserByPhoneNumber(signUpRequest.getPhoneNumber())) return ResponseEntity.badRequest().body(new SignUpResponse("Số điện thoại đã được sử dụng. Vui lòng thử lại!!!"));
        if (isValidPassword(signUpRequest.getPassword())) return ResponseEntity.badRequest().body(new SignUpResponse("Mật khẩu không hợp lệ!"));
        try {
            User user = new User(signUpRequest.getPhoneNumber(),signUpRequest.getPassword());
            userRepository.save(user);
            return ResponseEntity.ok(new SignUpResponse("Tạo tài khoản thành công. Mã OTP đã được gửi đến số điện thoại đăng ký"));
        } catch(Exception e){
            return ResponseEntity.badRequest().body(new SignUpResponse("Tạo tài khoản thất bại. Vui lòng thử lại!"));

        }


    }

    private boolean isValidPassword(String password) {
        return true;
    }
}
