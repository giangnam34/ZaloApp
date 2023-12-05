package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.enums.ResourceType;
import com.essay.zaloapp.domain.models.Post;
import com.essay.zaloapp.domain.models.PostUser;
import com.essay.zaloapp.domain.models.Resource;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.domain.payload.request.CreateNewPostRequest;
import com.essay.zaloapp.domain.payload.response.SocialMedia.GetInfoPostResponse;
import com.essay.zaloapp.domain.payload.response.findUserByPhoneNumberResponse;
import com.essay.zaloapp.repository.PostRepository;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.FileStorageService;
import com.essay.zaloapp.services.SocialMediaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialMediaServiceImpl implements SocialMediaService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public String createNewPost(Long userId, CreateNewPostRequest createNewPostRequest){
        try{
            User user = userRepository.findById(userId);
            List<Resource> resourceList = Arrays.stream(createNewPostRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image )).collect(Collectors.toList());
            Post post = new Post(new Date(new Date().getTime() + 7 * 60 * 60*1000), new Date(new Date().getTime() + 7 * 60 * 60*1000), createNewPostRequest.getContent(),createNewPostRequest.getAudience(),user, resourceList);
            List<PostUser> postUserList = createNewPostRequest.getUserTagIDList().stream().map(p -> new PostUser(post, userRepository.findByPhoneNumber(p), PostUserType.TagUser)).collect(Collectors.toList());
            post.setPostUserList(postUserList);
            postRepository.save(post);
            return "Đăng bài viết thành công!";
        } catch(Exception e){
            System.out.println(e.toString());
            return "Có lỗi trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String updateAudiencePost(Long userId, Long postId, Audience audience){
        try{
            Post post = postRepository.findById(postId).get();
            if (post == null || !post.getUser().getId().equals(userId)) throw new Exception("Bạn không có quyền chỉnh sửa bài viết này hoặc bài viết không còn tồn tại nữa! ");
            post.setAudienceValue(audience);
            postRepository.save(post);
            return "Đổi đối tượng bài viết thành công!";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String updatePost(Long userId, Long postId, CreateNewPostRequest createNewPostRequest){
        try{
            Post post = postRepository.findByIdAndUser_Id(postId, userId);
            if (post == null) throw new Exception("Bạn không có quyền chỉnh sửa bài viết này hoặc bài viết không còn tồn tại nữa!");
            post.setContentPost(createNewPostRequest.getContent());
            post.setAudienceValue(createNewPostRequest.getAudience());
            List<PostUser> postUserList = createNewPostRequest.getUserTagIDList().stream().map(p -> new PostUser(post, userRepository.findByPhoneNumber(p), PostUserType.TagUser)).collect(Collectors.toList());
            post.setPostUserList(postUserList);
            post.setUpdateAt(new Date(new Date().getTime() + 7*60*60*1000));
            List<Resource> resourceList = Arrays.stream(createNewPostRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image )).collect(Collectors.toList());
            post.setResourceList(resourceList);
            postRepository.save(post);
            return "Chỉnh sửa bài viết thành công!";
        } catch(Exception e){
            System.out.println(e.toString());
            return e.getMessage();
        }
    }

    @Override
    public GetAllInfoPost getAllPostUser(Long userId){
        try{
            return new GetAllInfoPost("Thành công!",postRepository.findByUser_Id(userId).stream().map(p -> {
                GetInfoPostResponse getInfoPostResponse = modelMapper.map(p, GetInfoPostResponse.class);
                getInfoPostResponse.setPostFather(modelMapper.map(p.getPostTop(), GetInfoPostResponse.class));
                getInfoPostResponse.setUserLikeList(p.getPostUserList().stream().filter(postUser -> postUser.getPostUserType().equals(PostUserType.UserLike)).map(postUser -> new findUserByPhoneNumberResponse(postUser.getUser().getFullName(),postUser.getUser().getImageAvatarUrl(), postUser.getUser().getPhoneNumber())).collect(Collectors.toList()));
                getInfoPostResponse.setUserTagList(p.getPostUserList().stream().filter(postUser -> postUser.getPostUserType().equals(PostUserType.TagUser)).map(postUser -> new findUserByPhoneNumberResponse(postUser.getUser().getFullName(),postUser.getUser().getImageAvatarUrl(), postUser.getUser().getPhoneNumber())).collect(Collectors.toList()));
                getInfoPostResponse.setUserShareList(p.getPostList().stream().map(postUser -> new findUserByPhoneNumberResponse(postUser.getUser().getFullName(),postUser.getUser().getImageAvatarUrl(), postUser.getUser().getPhoneNumber())).collect(Collectors.toList()));
                return getInfoPostResponse;
            }).collect(Collectors.toList()));
        } catch(Exception e){
            System.out.println(e.toString());
            return new GetAllInfoPost("Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!", null);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllInfoPost {
        private String mesage;
        private List<GetInfoPostResponse> getInfoPostResponse;
    }

}
