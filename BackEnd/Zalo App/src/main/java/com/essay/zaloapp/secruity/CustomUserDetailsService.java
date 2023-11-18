package com.essay.zaloapp.secruity;


import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phoneNumber)
            throws UsernameNotFoundException {
        System.out.println("CustomUserDetailsService: loadUserByUsername");
        System.out.println("Phone number: " + phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber);
        System.out.println("Roles: " + user.getRoles());
        if (user == null)
        {
            throw new RuntimeException("Tài khoản của bạn không tồn tại");
        }
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("Email của bạn không tồn tại : " + phoneNumber));
        if (Boolean.TRUE.equals(user.getIsLocked()))
        {
            throw new RuntimeException("Tài khoản của bạn đã bị khóa");
        }
        if (Boolean.FALSE.equals(user.getIsConfirmed())) {
            throw new RuntimeException("Tài khoản của bạn chưa đuợc xác thực!!!");
        }
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        System.out.println("CustomUserDetailsService: loadUserById");
        User user = userRepository.findById(id);
        if (user == null)
        {
            throw new RuntimeException("Tài khoản của bạn không tồn tại");
        }
//                .orElseThrow(
//                () -> new ResourceNotFoundException("User", "id", id)
//        );

        return UserPrincipal.create(user);
    }
}