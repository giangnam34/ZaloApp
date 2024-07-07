package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.User;

import com.essay.zaloapp.domain.payload.request.Authorize.ChangeInfoUserRequest;
import com.essay.zaloapp.domain.payload.request.Authorize.ChangePhoneNumberUserRequest;
import com.essay.zaloapp.domain.payload.response.Authorize.DetailInfoUser;
import com.essay.zaloapp.domain.payload.response.Authorize.GetUserResponse;
import com.essay.zaloapp.repository.FriendsRepository;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.FileStorageService;
import com.essay.zaloapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper mapper;

    @Override
    public void swap(User user1, User user2){
        User user3 = user2;
        user2 = user1;
        user1 = user3;
    }

    @Override
    public ResponseEntity<?> getUserById(Long id){
        try{
            User user = userRepository.findById(id);

            if(user != null){

                Set<String> roles = user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet());

                GetUserResponse response = mapper.map(user, GetUserResponse.class);
                response.setRoles(roles);

                return ResponseEntity.ok(response);
            }else{
                return ResponseEntity.badRequest().body(new GetUserResponse());
            }
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> updateImageAvatar(Long userId, MultipartFile file){
        try{
            if (file == null || file.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thiếu hình ảnh để cập nhật");
            User user = userRepository.findById(userId);
            if (file.getContentType().contains("video/mp4")) throw new Exception();
            user.setImageAvatarUrl(fileStorageService.storeFile(file));
            userRepository.save(user);
            return ResponseEntity.ok("Cập nhật ảnh đại diện thành công!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body("Có lỗi xảy ra trong quá trình cập nhật ảnh đại diện. Vui lòng thử lại!");
        }
    }

    @Override
    public Resource getImageAvatar(Long userId) throws Exception {
        try{
            User user = userRepository.findById(userId);
            return fileStorageService.loadFileAsResource(user.getImageAvatarUrl());
        } catch(Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public Resource getImageAvatar(User user) throws Exception {
        try{
            return fileStorageService.loadFileAsResource(user.getImageAvatarUrl());
        } catch(Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public Resource getImageAvatar(String phoneNumber) throws Exception {
        try{
            User user = userRepository.findByPhoneNumber(phoneNumber);
            return fileStorageService.loadFileAsResource(user.getImageAvatarUrl());
        } catch(Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> updateImageCoverAvatar(Long userId, MultipartFile file) {
        try{
            if (file == null || file.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thiếu hình ảnh để cập nhật");
            User user = userRepository.findById(userId);
            user.setImageCoverPhotoUrl(fileStorageService.storeFile(file));
            userRepository.save(user);
            return ResponseEntity.ok("Cập nhật ảnh bìa thành công!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body("Có lỗi xảy ra trong quá trình cập nhật ảnh bìa. Vui lòng thử lại!");
        }
    }

    @Override
    public Resource getImageCoverAvatar(Long userId) throws Exception {
        try{
            User user = userRepository.findById(userId);
            return fileStorageService.loadFileAsResource(user.getImageCoverPhotoUrl());
        } catch(Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public Resource getImageCoverAvatar(String phoneNumber) throws Exception {
        try{
            User user = userRepository.findByPhoneNumber(phoneNumber);
            return fileStorageService.loadFileAsResource(user.getImageCoverPhotoUrl());
        } catch(Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public Resource getImageCoverAvatar(User user) throws Exception {
        try{
            return fileStorageService.loadFileAsResource(user.getImageCoverPhotoUrl());
        } catch(Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> updatePhoneNumberUser(Long userId, ChangePhoneNumberUserRequest changePhoneNumberUserRequest) throws Exception{
        try {
            User user = userRepository.findById(userId);
            if (userRepository.existsUserByPhoneNumber(changePhoneNumberUserRequest.getNewPhoneNumber())) return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ. Vui lòng thử lại");
            user.setPhoneNumber(changePhoneNumberUserRequest.getNewPhoneNumber());
            userRepository.save(user);
            return ResponseEntity.ok("Cập nhật số điện thoại thành công!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> updateInfoUser(Long userId, ChangeInfoUserRequest changeInfoUserRequest) throws Exception{
        try {
            User user = userRepository.findById(userId);
            if (changeInfoUserRequest.getFullName() != null) user.setFullName(changeInfoUserRequest.getFullName());
            if (changeInfoUserRequest.getSex() != null) user.setSex(changeInfoUserRequest.getSex());
            if (changeInfoUserRequest.getBirthday() != null) user.setBirthDay(changeInfoUserRequest.getBirthday());
            userRepository.save(user);
            return ResponseEntity.ok("Cập nhật thông tin cá nhân thành công!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> findUserByPhoneNumber(String phoneNumber) throws Exception{
        try {
            User user = userRepository.findByPhoneNumber(phoneNumber);
            if (user == null || !user.getIsConfirmed() || user.getIsLocked()) return ResponseEntity.badRequest().body(" Số điện thoại chưa đăng ký tài khoản hoặc không cho phép tìm kiếm!");
            DetailInfoUser detailInfoUser = new DetailInfoUser(user.getFullName(), "http://localhost:8181/v1/users/imageAvatarAnotherUser/" + phoneNumber, "http://localhost:8181/v1/users/imageCoverAvatarAnotherUser/" + phoneNumber, phoneNumber, user.getSex().name(),user.getBirthDay());
            return ResponseEntity.ok(detailInfoUser);
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> findUserById(Long userId) throws Exception{
        try {
            User user = userRepository.findById(userId);
            if (user == null || !user.getIsConfirmed() || user.getIsLocked()) return ResponseEntity.badRequest().body(" Số điện thoại chưa đăng ký tài khoản hoặc không cho phép tìm kiếm!");
            DetailInfoUser detailInfoUser = new DetailInfoUser(user.getFullName(), "http://localhost:8181/v1/users/imageAvatarAnotherUser/" + user.getPhoneNumber(), "http://localhost:8181/v1/users/imageCoverAvatarAnotherUser/" + user.getPhoneNumber(), user.getPhoneNumber(), user.getSex().name(),user.getBirthDay());
            return ResponseEntity.ok(detailInfoUser);
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

}
