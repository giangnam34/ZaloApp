package com.essay.zaloapp.controller;

import com.essay.zaloapp.services.impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;
    @GetMapping("/valid/{password}")
    public Object testValidPassword(@PathVariable String password){
        return authenticationService.isValidPassword(password);
    }

    @GetMapping("/generateOTP/{minRange}/{maxRange}")
    public Object testGenerateOTP(@PathVariable int minRange, @PathVariable int maxRange){
        return authenticationService.generateOTPCode(minRange,maxRange);
    }

    @GetMapping("/sendOTP/{phoneNumber}")
    public Object testSendOTP(@PathVariable String phoneNumber){
        return authenticationService.sendOTP(phoneNumber);
    }


}
