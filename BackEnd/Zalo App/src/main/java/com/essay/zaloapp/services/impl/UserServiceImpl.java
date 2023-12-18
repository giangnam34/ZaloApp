package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.FriendStatus;
import com.essay.zaloapp.domain.models.Composite.FriendsId;
import com.essay.zaloapp.domain.models.Friends;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.ChangeInfoUserRequest;
import com.essay.zaloapp.domain.payload.request.ChangePhoneNumberUserRequest;
import com.essay.zaloapp.domain.payload.request.FriendRequest;
import com.essay.zaloapp.domain.payload.response.Authorize.DetailInfoUser;
import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import com.essay.zaloapp.domain.payload.response.GetAllInviteFriendResponse;
import com.essay.zaloapp.domain.payload.response.GetUserResponse;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<?> sendInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body(" Số điện thoại chưa đăng ký tài khoản hoặc không cho phép gửi lời mời kết bạn!");
            if (user1.getId() > user2.getId()){
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            if (friendsRepository.existsFriendsByFriendsId(new FriendsId(user1.getId(),user2.getId()))){
                Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(),user2.getId()));
                if (friends.getFriendStatus().equals(FriendStatus.WaitingAccept)){
                    if (!friends.getSendInviteBy() && Objects.equals(user1.getId(), userId))
                        return ResponseEntity.badRequest().body("Đã gửi lời mời kết bạn từ trước. Vui lòng chờ người dùng phản hồi.");
                    else if (friends.getSendInviteBy() && Objects.equals(user2.getId(), userId))
                        return ResponseEntity.badRequest().body("Đã có lời mời kết bạn từ trước. Vui lòng chấp nhận hoặc hủy lời mời!");
                }
                else if (friends.getFriendStatus().equals(FriendStatus.IsFriend)){
                    return ResponseEntity.badRequest().body("Hai người đã là bạn từ trước!");
                }
                else if (friends.getFriendStatus().equals(FriendStatus.ISBlock)){
                    if ((friends.getIsBlock() == 1 && Objects.equals(user1.getId(), userId)) || (friends.getIsBlock() == 2 && Objects.equals(user2.getId(), userId)))
                        return ResponseEntity.badRequest().body("Bạn đã chặn người dùng này. Vui lòng bỏ chặn để gửi lời mời kết bạn!");
                    else
                        return ResponseEntity.badRequest().body("Người dùng đã chặn bạn! Xin thứ lỗi!");
                }
            }
            Friends friend = new Friends(new FriendsId(user1.getId(),user2.getId()), null, 0L, 0L, user2.getFullName(), user1.getFullName(), !Objects.equals(user1.getId(), userId), FriendStatus.WaitingAccept);
            friend.setUser1(user1);
            friend.setUser2(user2);
            friendsRepository.save(friend);
            return ResponseEntity.ok("Gửi lời mời kết bạn thành công!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> acceptingInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()){
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(),user2.getId()));
            System.out.println(friends.getSendInviteBy());
            if (!friends.getSendInviteBy() && Objects.equals(user1.getId(), userId) || friends.getSendInviteBy() && Objects.equals(user2.getId(), userId))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (friends.getFriendStatus().equals(FriendStatus.WaitingAccept)){
                if (addFriendRequest.getIsAcceptingInvite()) {
                    friends.setFriendStatus(FriendStatus.IsFriend);
                    friends.setFriendFrom(new Date(new Date().getTime() + 7 * 60 * 60000));
                    friendsRepository.save(friends);
                    return ResponseEntity.ok("Chấp nhận lời mời kết bạn thành công!!!");
                }
                else {
                    friendsRepository.delete(friends);
                    return ResponseEntity.ok("Không chấp nhận lời mời thành công!!!");
                }
            }
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> cancelInviteFriend(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()){
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(),user2.getId()));
            if ( friends == null || (!friends.getSendInviteBy() && Objects.equals(user1.getId(), userId)) || (friends.getSendInviteBy() && Objects.equals(user2.getId(), userId)) )
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (friends.getFriendStatus().equals(FriendStatus.WaitingAccept)) {
                friendsRepository.delete(friends);
                return ResponseEntity.ok("Hủy lời mời kết bạn thành công!!!");
            }
            return ResponseEntity.badRequest().body("Không thể hủy lời mời kết bạn!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> unFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()){
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(),user2.getId()));
            if (friends.getFriendStatus().equals(FriendStatus.IsFriend)){
                friends.setFriendStatus(FriendStatus.IsUnFriend);
                friends.setIsDelete(Objects.equals(userId, user1.getId()) ? 1L : 2L);
                friendsRepository.save(friends);
            }
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> blockFriendUser(Long userId, FriendRequest addFriendRequest) throws Exception {
        try {
            User user1 = userRepository.findById(userId);
            if (!user1.getPhoneNumber().equals(addFriendRequest.getFromPhoneNumberUser()) || addFriendRequest.getFromPhoneNumberUser().equals(addFriendRequest.getToPhoneNumberUser()))
                return ResponseEntity.status(401).body("Bạn không có quyền để thực hiện hành động này!");
            if (addFriendRequest.getToPhoneNumberUser().length() < 10)
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ!");
            User user2 = userRepository.findByPhoneNumber(addFriendRequest.getToPhoneNumberUser());
            if (user2 == null)
                return ResponseEntity.badRequest().body("Hành động không hợp lệ!!!");
            if (user1.getId() > user2.getId()){
                User userTMP = new User();
                userTMP = user1;
                user1 = user2;
                user2 = userTMP;
            }
            if (friendsRepository.existsFriendsByFriendsId(new FriendsId(user1.getId(),user2.getId()))){
                Friends friends = friendsRepository.findByFriendsId(new FriendsId(user1.getId(),user2.getId()));
                if (friends.getFriendStatus().equals(FriendStatus.ISBlock) && ((friends.getIsBlock() == 1 && Objects.equals(user2.getId(), userId)) || (friends.getIsBlock() == 2 && Objects.equals(user1.getId(), userId)))){
                    return ResponseEntity.badRequest().body("Người dùng đã block bạn từ trước!!!");
                }
                friends.setFriendStatus(FriendStatus.ISBlock);
                friends.setIsBlock(Objects.equals(user1.getId(), userId) ? 1L : 2L);
                return ResponseEntity.ok("Chặn người dùng này thành công!!!");
            }
            Friends friends = new Friends(new FriendsId(user1.getId(),user2.getId()), null, Objects.equals(user1.getId(), userId) ? 1L : 2L, 0L, user2.getFullName(), user1.getFullName(), null, FriendStatus.ISBlock);
            friendsRepository.save(friends);
            return ResponseEntity.ok("Chặn người dùng này thành công!!!");
        } catch (Exception e){
            throw new Exception("Có lỗi xảy ra. Vui lòng thử lại!!!");
        }
    }

    @Override
    public ResponseEntity<?> listAllFriend(Long userId) throws Exception{
        try{
            List<Friends> friendsList = friendsRepository.findByFriendsIdUser1(userId);
            friendsList.addAll(friendsRepository.findByFriendsIdUser2(userId));
            List<InfoUser> result = new ArrayList<>();
            for (Friends friend : friendsList) {
                if (friend.getFriendStatus().equals(FriendStatus.IsFriend))
                    result.add(new InfoUser(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getFullName() : friend.getUser1().getFullName(),
                            "http://localhost:8181/v1/users/imageAvatarAnotherUser/" + (Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber()),
                            Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber()));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!");
        }
    }

    @Override
    public ResponseEntity<?> listAllInviteFriend(Long userId) throws Exception{
        try{
            User user = userRepository.findById(userId);
            List<Friends> friendsList = friendsRepository.findByFriendsIdUser1(userId);
            friendsList.addAll(friendsRepository.findByFriendsIdUser2(userId));
            List<GetAllInviteFriendResponse> result = new ArrayList<>();
            for (Friends friend : friendsList) {
                if (friend.getFriendStatus().equals(FriendStatus.WaitingAccept))
                    result.add(new GetAllInviteFriendResponse(Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getFullName() : friend.getUser1().getFullName(),
                            "http://localhost:8181/v1/users/imageAvatarAnotherUser/" + (Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber()),
                            Objects.equals(userId, friend.getUser1().getId()) ? friend.getUser2().getPhoneNumber() : friend.getUser1().getPhoneNumber(),
                            (friend.getSendInviteBy() && Objects.equals(userId, friend.getUser2().getId())) ||
                                    (!friend.getSendInviteBy() && Objects.equals(userId, friend.getUser1().getId()))));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Có lỗi xảy ra. Vui lòng thử lại!");
        }
    }

}
