package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.RoleName;
import com.essay.zaloapp.domain.models.OTPCode;
import com.essay.zaloapp.domain.models.Role;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.AuthorizeOTPResponse;
import com.essay.zaloapp.domain.payload.request.ForgetPasswordRequest;
import com.essay.zaloapp.domain.payload.request.LoginRequest;
import com.essay.zaloapp.domain.payload.request.SignUpRequest;
import com.essay.zaloapp.domain.payload.response.LoginResponse;
import com.essay.zaloapp.domain.payload.response.ResultSMSResponse;
import com.essay.zaloapp.domain.payload.response.ResultSendSMSResponse;
import com.essay.zaloapp.domain.payload.response.SignUpResponse;
import com.essay.zaloapp.repository.RoleRepository;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.secruity.CustomUserDetailsService;
import com.essay.zaloapp.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
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
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) throws Exception {
        if (!signUpRequest.getPassword().equals(signUpRequest.getReEnterPassword()))
            return ResponseEntity.badRequest().body(new SignUpResponse("Mật khẩu không trùng với Xác nhận mật khẩu!"));
        if (userRepository.existsUserByPhoneNumber(signUpRequest.getPhoneNumber()))
            return ResponseEntity.badRequest().body(new SignUpResponse("Số điện thoại đã được sử dụng. Vui lòng thử lại!!!"));
        if (!isValidPassword(signUpRequest.getPassword()))
            return ResponseEntity.badRequest().body(new SignUpResponse("Mật khẩu không hợp lệ!"));
        try {
            HashSet<Role> roleUser = new HashSet<Role>();
            roleUser.add((Role) roleRepository.findByName(RoleName.ROLE_USER));
            String otpCode = generateOTPCode(100000, 999999);
            User user = new User(signUpRequest.getPhoneNumber(), signUpRequest.getFullName(), passwordEncoder.encode(signUpRequest.getPassword()), roleUser);
            ResultSMSResponse result = new ObjectMapper().readValue(sendOTP(signUpRequest.getPhoneNumber(), otpCode), ResultSMSResponse.class);
            userRepository.save(user);
            Long statusCode = getStatusSendOTP(result);
            if (statusCode == 2)
                return ResponseEntity.badRequest().body(new SignUpResponse("Hmm Có lỗi trong quá trình gửi OTP. Vui lòng thử lại"));
            user.setOtpCode(new OTPCode(user, com.essay.zaloapp.domain.enums.OTP.AuthorizeAccount, otpCode, new Date(new Date().getTime() + 7 * 60 * 60000 + 4 * 60000)));
            if (statusCode == 0 || statusCode == -1)
                return ResponseEntity.ok(new SignUpResponse("Hệ thống đang gửi mã OTP. Xin vui lòng chờ trong ít phút"));
            return ResponseEntity.ok(new SignUpResponse("Hệ thống đã gửi mã OTP tới số điện thoại. Xin vui lòng kiểm tra và nhập mã"));
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new Exception("Tạo tài khoản thất bại. Vui lòng thử lại!");
        }
    }

    @Override
    public String sendOTP(String toPhoneNumber, String valueOTP) {
        SpeedSMSAPI api = new SpeedSMSAPI();
        String content = "Mã OTP xác thực của bạn là: " + valueOTP;
        int type = 5;
        String sender = "d4b7294f8ce8412a";
        try {
            return api.sendSMS(toPhoneNumber, content, type, sender);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Long getStatusSendOTP(ResultSMSResponse result) throws InterruptedException, IOException {
        SpeedSMSAPI api = new SpeedSMSAPI();
        TimeUnit.SECONDS.sleep(3);
        ResultSendSMSResponse resultSend = new ObjectMapper().readValue(api.getStatusMessage(String.valueOf(result.getData().getTranId())), ResultSendSMSResponse.class);
        return resultSend.getData().get(0).getStatus();
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
    public String generateOTPCode(int minRange, int maxRange) {
        int result = (int) Math.floor(Math.random() * (maxRange - minRange + 1) + minRange);
        return String.valueOf(result);
    }

    @Override
    public String formatPhoneNumbertoE164(String phoneNumber) {
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]+", "");

        if (cleanedNumber.startsWith("0")) {
            cleanedNumber = "+84" + cleanedNumber.substring(1);
        }

        if (cleanedNumber.length() == 10) {
            cleanedNumber = "+84" + cleanedNumber;
        }

        return cleanedNumber;
    }

    @Override
    public ResponseEntity<?> authorizeOTP(AuthorizeOTPResponse authorizeOTPResponse) {
        if (!userRepository.existsUserByPhoneNumber(authorizeOTPResponse.getPhoneNumber()))
            return ResponseEntity.badRequest().body("Người dùng chưa đăng ký tài khoản!");
        User user = userRepository.findByPhoneNumber(authorizeOTPResponse.getPhoneNumber());
        if (!user.getOtpCode().getValue().equals(authorizeOTPResponse.getOtpCode()))
            return ResponseEntity.badRequest().body("Mã OTP không đúng!");
        if (!user.getOtpCode().getExpireTime().after(new Date(new Date().getTime() + 7 * 60 * 60000)))
            return ResponseEntity.badRequest().body("Mã OTP hết hạn!");
        user.setIsConfirmed(true);
        userRepository.save(user);
        return ResponseEntity.ok("Xác thực tài khoản thành công!");
    }

    @Override
    public ResponseEntity<?> forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        ResponseEntity<?> result = authorizeOTP(new AuthorizeOTPResponse(forgetPasswordRequest.getPhoneNumber(), forgetPasswordRequest.getOtpCode()));
        if (result.getStatusCodeValue() != 200)
            return result;
        if (!forgetPasswordRequest.getNewPassword().equals(forgetPasswordRequest.getReEnterPassword()))
            return ResponseEntity.badRequest().body("Mật khẩu và xác nhận mật khẩu không khớp!!!");
        if (!isValidPassword(forgetPasswordRequest.getNewPassword()))
            return ResponseEntity.badRequest().body("Mật khẩu không đủ mạnh!!!");
        try {
            User user = userRepository.findByPhoneNumber(forgetPasswordRequest.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(forgetPasswordRequest.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("Đổi mật khẩu thành công!!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }
}
