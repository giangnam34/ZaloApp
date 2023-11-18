package com.essay.zaloapp.controller;

import com.essay.zaloapp.Constant.Message;
import com.essay.zaloapp.secruity.UserPrincipal;
import com.essay.zaloapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id, UserPrincipal principal){
        if (!(principal.getId() == id)) return ResponseEntity.badRequest().body(Message.WRONG_USERID);
        return userService.getUserById(id);
    }
}
