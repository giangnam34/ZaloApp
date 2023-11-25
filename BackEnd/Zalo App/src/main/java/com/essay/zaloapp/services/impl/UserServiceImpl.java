package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.ChangePhoneNumberUserRequest;
import com.essay.zaloapp.domain.payload.response.GetUserResponse;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public ResponseEntity<?> updateImageAvatar(Long userId, MultipartFile file){
        try{
            User user = userRepository.findById(userId);
            user.setImageAvatarUrl(file.getBytes());
            userRepository.save(user);
            return ResponseEntity.ok("Cập nhật ảnh đại diện thành công!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body("Có lỗi xảy ra trong quá trình cập nhật ảnh đại diện. Vui lòng thử lại!");
        }
    }

    @Override
    public ResponseEntity<Resource> getImageAvatar(Long userId) throws Exception {
        try{
            User user = userRepository.findById(userId);
            return ResponseEntity.ok(new ByteArrayResource(user.getImageAvatarUrl()));
        } catch(Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> updateImageCoverAvatar(Long userId, MultipartFile file) {
        try{
            User user = userRepository.findById(userId);
            user.setImageCoverPhotoUrl(file.getBytes());
            userRepository.save(user);
            return ResponseEntity.ok("Cập nhật ảnh đại diện thành công!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body("Có lỗi xảy ra trong quá trình cập nhật ảnh bìa. Vui lòng thử lại!");
        }
    }

    @Override
    public ResponseEntity<Resource> getImageCoverAvatar(Long userId) throws Exception {
        try{
            User user = userRepository.findById(userId);
            return ResponseEntity.ok(new ByteArrayResource(user.getImageCoverPhotoUrl()));
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
            return ResponseEntity.ok("Cập nhật số điện thoại thành công!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

}
