package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.enums.Status;
import com.essay.zaloapp.domain.models.*;
import com.essay.zaloapp.domain.payload.response.Admin.GeneralInfoResponse;
import com.essay.zaloapp.domain.payload.response.Admin.GroupsResponse;
import com.essay.zaloapp.domain.payload.response.Admin.PostsResponse;
import com.essay.zaloapp.domain.payload.response.Admin.UsersResponse;
import com.essay.zaloapp.repository.*;
import com.essay.zaloapp.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private GroupChatUserRepository groupChatUserRepository;

    @Autowired
    private MessageChatRepository messageChatRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<UsersResponse> getAllUsersForAdmin() {
        Iterable<User> iterable = userRepository.findAll();
        List<User> users = new ArrayList<>();
        iterable.forEach(users::add);

        users = users
                .stream()
                .filter(user -> !user.getIsLocked())
                .collect(Collectors.toList());

        List<UsersResponse> usersResponses = new ArrayList<>();
        for (User user : users) {
            UsersResponse usersResponse = new UsersResponse();
            usersResponse.setId(user.getId());
            usersResponse.setFullName(user.getFullName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String birthDay = dateFormat.format(user.getBirthDay());
            usersResponse.setBirthDay(birthDay);
            usersResponse.setIsConfirm(user.getIsConfirmed());
            usersResponse.setIsLock(user.getIsLocked());

            List<Post> posts = postRepository.findByUser_Id(user.getId(), PageRequest.of(1,100));
            usersResponse.setPostsCount(Long.valueOf(posts.size()));

            List<Friends> friends = friendsRepository.findAllByUserId(user.getId());
            usersResponse.setFriendsCount(Long.valueOf(friends.size()));

            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findByIdPhoneNumberUser(user.getPhoneNumber());
            usersResponse.setGroupsCount(Long.valueOf(groupChatUsers.size()));

            usersResponses.add(usersResponse);
        }

        return usersResponses;
    }

    @Override
    public List<PostsResponse> getAllPostsForAdmin() {
        List<Post> posts = postRepository.findAll();

        List<PostsResponse> postsResponses = new ArrayList<>();
        for (Post post : posts) {
            PostsResponse postsResponse = new PostsResponse();
            postsResponse.setId(post.getId());
            postsResponse.setContent(post.getContentPost());
            postsResponse.setAudience(post.getAudienceValue().name());

            User user = userRepository.findById(post.getUser().getId());
            if (user != null) {
                postsResponse.setPostUserName(user.getFullName());
                postsResponse.setPostUserPhoneNumber(user.getPhoneNumber());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String createdDate = dateFormat.format(post.getCreatedAt());
            postsResponse.setCreatedDate(createdDate);

            postsResponse.setCommentsCount((long) (int) postRepository.findById(post.getId()).get().getCommentList().stream().filter(comment -> !comment.getIsDelete()).count());

            long likesCount = post.getPostUserList().stream()
                    .filter(postUser -> postUser.getPostUserType() == PostUserType.UserLike)
                    .count();
            postsResponse.setLikesCount(likesCount);

            postsResponses.add(postsResponse);
        }

        return postsResponses;
    }

    @Override
    public List<GroupsResponse> getAllGroupsForAdmin() {
        List<GroupChat> groupChatList = groupChatRepository.findAll()
                .stream()
                .filter(groupChat -> !groupChat.getIsDeleted())
                .collect(Collectors.toList());

        List<GroupsResponse> groupsResponses = new ArrayList<>();
        for (GroupChat groupChat : groupChatList) {
            GroupsResponse groupsResponse = new GroupsResponse();
            groupsResponse.setId(groupChat.getId());
            groupsResponse.setGroupName(groupChat.getGroupName());

            List<GroupChatUser> groupChatUsers = groupChatUserRepository.findAllByGroupId(groupChat.getId())
                    .stream()
                    .filter(groupChatUser -> !groupChatUser.getIsDeleted())
                    .collect(Collectors.toList());
            groupsResponse.setMembersCount(Long.valueOf(groupChatUsers.size()));

            List<MessageChat> messageChats = messageChatRepository.findAllByGroupIdNoPagination(groupChat.getId())
                    .stream()
                    .filter(messageChat -> !messageChat.getDeleted())
                    .collect(Collectors.toList());
            groupsResponse.setMessagesCount(Long.valueOf(messageChats.size()));

            groupsResponses.add(groupsResponse);
        }

        return groupsResponses;
    }

    @Override
    public GeneralInfoResponse getGeneralInfo() {
        Iterable<User> iterable = userRepository.findAll();
        List<User> users = new ArrayList<>();
        iterable.forEach(users::add);

        users = users
                .stream()
                .filter(user -> !user.getIsLocked())
                .collect(Collectors.toList());

        GeneralInfoResponse generalInfoResponse = new GeneralInfoResponse();
        generalInfoResponse.setMembersCount(Long.valueOf(users.size()));

        List<User> onlineUsers = userRepository.findAllByStatus(Status.ONLINE);
        generalInfoResponse.setOnlineMembersCount(Long.valueOf(onlineUsers.size()));

        List<GroupChat> groupChatList = groupChatRepository.findAll()
                .stream()
                .filter(groupChat -> !groupChat.getIsDeleted())
                .collect(Collectors.toList());
        generalInfoResponse.setGroupsCount(Long.valueOf(groupChatList.size()));

        List<Post> postList = postRepository.findAll();
        generalInfoResponse.setPostsCount(Long.valueOf(postList.size()));

        return generalInfoResponse;
    }

    @Override
    public String deleteGroupChatForAdmin(Long groupId) {
        try {
            Optional<GroupChat> groupChatOptional = groupChatRepository.findById(groupId);
            if (groupChatOptional.isEmpty()) {
                return "Nhóm cần xóa không tồn tại!";
            }

            GroupChat groupChat = groupChatOptional.get();

            List<GroupChatUser> groupChatUserList = groupChatUserRepository.findAllByGroupId(groupId);
            for (GroupChatUser groupChatUser : groupChatUserList) {
                groupChatUser.setIsDeleted(true);
                groupChatUserRepository.save(groupChatUser);
            }

            groupChat.setIsDeleted(true);
            groupChatRepository.save(groupChat);
            return "Xóa nhóm thành công!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra. Vui lòng thử lại!";
        }
    }

    @Override
    public String deletePostForAdmin(Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(Exception::new);
            postRepository.delete(post);
            return "Xóa bài viết thành công!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra. Vui lòng thử lại!";
        }
    }

    @Override
    public String lockUserForAdmin(Long userId) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                return "Người dùng cần khóa không tồn tại!";
            } else {
                user.setIsLocked(true);
                userRepository.save(user);
                return "Khóa người dùng thành công!";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra. Vui lòng thử lại!";
        }
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public class GetAllUsers {
//        private String message;
//        private List<UsersResponse> usersResponses;
//    }
}
