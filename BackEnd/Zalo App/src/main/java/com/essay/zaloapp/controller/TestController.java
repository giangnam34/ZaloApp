package com.essay.zaloapp.controller;

import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.SocialMedia.CreateNewPostRequest;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.RecommendSystemService;
import com.essay.zaloapp.services.SocialMediaService;
import com.essay.zaloapp.services.impl.AuthenticationServiceImpl;
import com.essay.zaloapp.services.impl.SocialMediaServiceImpl;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private RecommendSystemService recommendSystemService;

    @Autowired
    private SocialMediaService socialMediaService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/valid/{password}")
    public Object testValidPassword(@PathVariable String password){
        return authenticationService.isValidPassword(password);
    }

    @GetMapping("/generateOTP/{minRange}/{maxRange}")
    public Object testGenerateOTP(@PathVariable int minRange, @PathVariable int maxRange){
        return authenticationService.generateOTPCode(minRange,maxRange);
    }

    @GetMapping("/test")
    public void test() throws IOException, TasteException {
        recommendSystemService.recommendSystem(1L, 30L);
    }

//    @GetMapping("/sendOTP/{phoneNumber}")
//    public Object testSendOTP(@PathVariable String phoneNumber){
//        return authenticationService.sendOTP(phoneNumber);
//    }

    @GetMapping("/generateDataPost")
    public void generateDataLikePost(){
        try {
            Random random = new Random();
            for (int q = 0; q < 5; q++) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 1; j < 20; j++) {
                        long userId = i * 20 + j;
                        int luckyNumber = random.nextInt(3);
                        for (int k = 1; k <= 5; k++) {
                            long postId = 1 + i * 200 + random.nextInt(200);// Random giá trị từ 1 đến 101
                            if (luckyNumber % 3 == 0) {
                                socialMediaService.likePost(postId, userId);
                            } else if (luckyNumber % 3 == 1) {
                                socialMediaService.likePost(postId, userId);
                                socialMediaService.createNewComment(postId, userId, null, String.format("Init data %s", i), null);
                            } else {
                                socialMediaService.likePost(postId, userId);
                                socialMediaService.createNewComment(postId, userId, null, String.format("Init data %s", i), null);
                                CreateNewPostRequest createNewPostRequest = CreateNewPostRequest.builder()
                                        .postTopId(postId)
                                        .audience("Public")
                                        .content(String.format("Init data %s", i))
                                        .build();
                                socialMediaService.createNewPost(userId, createNewPostRequest);
                            }
                        }

                    }
                }
            }
//            for (int i = 0; i < 10000; i++) {
//                long userId = 1 + random.nextInt(100); // Random giá trị từ 1 đến 500
//                long postId = 1 + random.nextInt(1000); // Random giá trị từ 1 đến 101
//                socialMediaService.likePost(postId, userId);
//            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//    @GetMapping("/generateDataCommentPost")
//    public void generateDataCommentPost(){
//        try {
//            Random random = new Random();
//
//            for (int i = 0; i < 10000; i++) {
//                long userId = 1 + random.nextInt(100); // Random giá trị từ 1 đến 500
//                long postId = 1 + random.nextInt(1000); // Random giá trị từ 1 đến 101
//                socialMediaService.createNewComment(postId,userId,null, String.format("Init data %s", i), null);
//            }
//
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @GetMapping("/generateDataSharePost")
//    public void generateDataSharePost(){
//        try {
//            Random random = new Random();
//
//            for (int i = 0; i < 10000; i++) {
//                long userId = 1 + random.nextInt(100); // Random giá trị từ 1 đến 500
//                long postId = 1 + random.nextInt(1000); // Random giá trị từ 1 đến 101
//                CreateNewPostRequest createNewPostRequest = CreateNewPostRequest.builder()
//                        .postTopId(postId)
//                        .audience("Public")
//                        .content(String.format("Init data %s", i))
//                        .build();
//                socialMediaService.createNewPost(userId,createNewPostRequest);
//            }
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }

    @GetMapping("/updateBirthDayUser")
    public void updateBirthDayUser(){
        try {
            Random random = new Random();
            List<User> userList = userRepository.findAll();
            int i = 0;
            for (User user : userList){
                Date birthDay = user.getBirthDay();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(birthDay);

                // Sinh số ngẫu nhiên từ 1960 đến 2010
                int randomYear = 1924 + i++; // 2010 - 1960 + 1 = 51

                randomYear = Math.min(randomYear,2006);

                // Cập nhật năm mới cho trường birthDay
                calendar.set(Calendar.YEAR, randomYear);

                // Cập nhật lại giá trị birthDay trong đối tượng user
                user.setBirthDay(calendar.getTime());
            }

            for (User user : userList){
                Date createdAt = user.getCreatedAt();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(createdAt);

                // Sinh số ngẫu nhiên từ 1960 đến 2010
                int randomYear = 2010 + random.nextInt(13); // 2010 - 1960 + 1 = 51

                // Cập nhật năm mới cho trường birthDay
                calendar.set(Calendar.YEAR, randomYear);

                // Cập nhật lại giá trị birthDay trong đối tượng user
                user.setCreatedAt(calendar.getTime());
            }
            userRepository.saveAll(userList);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
