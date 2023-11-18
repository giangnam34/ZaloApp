package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.Role;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.LoginRequest;
import com.essay.zaloapp.domain.payload.request.SignUpRequest;
import com.essay.zaloapp.domain.payload.response.LoginResponse;
import com.essay.zaloapp.domain.payload.response.SignUpResponse;
import com.essay.zaloapp.repository.RoleRepository;
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

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final String ACCOUNT_SID = "AC5d0901b177a1a4b888eb9921aabd00dc";
    private final String AUTH_TOKEN = "d22cea167dcda543b955629ce5e684ad";

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
    @Autowired
    private RoleRepository roleRepository;

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
        if (!isValidPassword(signUpRequest.getPassword())) return ResponseEntity.badRequest().body(new SignUpResponse("Mật khẩu không hợp lệ!"));
        try {
            HashSet<Role> roleUser = new HashSet<Role>();
            roleUser.add((Role)roleRepository.findByName("ROLE_USER"));
            User user = new User(signUpRequest.getPhoneNumber(),signUpRequest.getPassword(), roleUser);
            userRepository.save(user);
//            String result = sendOTP("0968322444");
//            System.out.println(result);
//            return ResponseEntity.ok(result);
            return ResponseEntity.ok(new SignUpResponse("Tạo tài khoản thành công. Mã OTP đã được gửi đến số điện thoại đăng ký"));
        } catch(Exception e){
            System.out.println(e.toString());
            return ResponseEntity.badRequest().body(new SignUpResponse("Tạo tài khoản thất bại. Vui lòng thử lại!"));

        }


    }

    @Override
    public boolean isValidPassword(String password) {
        System.out.println("Password: " + password);
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        System.out.println(m.matches());
        return m.matches();
    }

    @Override
    public String sendOTP(String toPhoneNumber){
        SpeedSMSAPI api  = new SpeedSMSAPI();
        String content = "Mã OTP xác thực của bạn là: " + generateOTPCode(100000,999999);
        int type = 5;
        String sender = "d4b7294f8ce8412a";
        try {
            String response = api.sendSMS(toPhoneNumber,content, type, sender);
            return response;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String generateOTPCode(int minRange, int maxRange){
        int result = (int)Math.floor(Math.random() * (maxRange - minRange + 1) + minRange);
        return String.valueOf(result);
    }

    @Override
    public String formatPhoneNumbertoE164(String phoneNumber){
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]+", "");

        if (cleanedNumber.startsWith("0")) {
            cleanedNumber = "+84" + cleanedNumber.substring(1);
        }

        if (cleanedNumber.length() == 10) {
            cleanedNumber = "+84" + cleanedNumber;
        }

        return cleanedNumber;
    }
}
