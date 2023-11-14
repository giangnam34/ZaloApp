package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.payload.request.TestSomeThing;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(Principal userPrincipal){
        return userPrincipal.getName();
    }

    @PostMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public String test(@Valid @RequestBody TestSomeThing test){
        return test.getUserName().equals("0968322444") ? "Test thanh cong" : "Test that bai";
    }
}
