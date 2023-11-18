package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.response.GetUserResponse;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ResponseEntity<?> getUserById(Long id){
        try{
            User user = userRepository.findById(id);

            if(user != null){
                return ResponseEntity.ok(mapper.map(user, GetUserResponse.class));
            }else{
                return ResponseEntity.badRequest().body(new GetUserResponse());
            }
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
