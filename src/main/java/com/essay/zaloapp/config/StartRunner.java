//package com.essay.zaloapp.config;
//
//import com.essay.zaloapp.domain.enums.Sex;
//import com.essay.zaloapp.domain.models.CategoryUser;
//import com.essay.zaloapp.domain.models.User;
//import com.essay.zaloapp.domain.models.UserOptionPoll;
//import com.essay.zaloapp.repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//
//@Component
//@AllArgsConstructor
//public class StartRunner implements ApplicationRunner {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        User user = new User("0968322444", "Giangnam", new Date(), Sex.Male, "", "", new Date(), new Date(),
//                new Date(), false, true, new HashSet<>(), new CategoryUser(), new UserOptionPoll(), new ArrayList<>());
//
//        userRepository.save(user);
//    }
//}
