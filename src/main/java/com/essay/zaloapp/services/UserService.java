package com.essay.zaloapp.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface UserService {
    ResponseEntity<?> getUserById(Long id);
}
