package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.enums.ResourceType;
import com.essay.zaloapp.domain.models.*;
import com.essay.zaloapp.domain.payload.request.CreateNewPostRequest;
import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import com.essay.zaloapp.domain.payload.response.SocialMedia.Comment.InfoComment;
import com.essay.zaloapp.domain.payload.response.SocialMedia.GetInfoPostResponse;
import com.essay.zaloapp.repository.*;
import com.essay.zaloapp.services.FileStorageService;
import com.essay.zaloapp.services.SocialMediaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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

    @Autowired
    private PostUserRepository postUserRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentUserRepository commentUserRepository;

    @Override
    public String validateCreateNewPostRequest(User user, CreateNewPostRequest createNewPostRequest){
        if ( (createNewPostRequest.getFiles() == null || (createNewPostRequest.getFiles() != null && createNewPostRequest.getFiles()[0].isEmpty())) && (createNewPostRequest.getContent() == null || createNewPostRequest.getContent().isEmpty()) && ( createNewPostRequest.getUserTagIDList() == null || createNewPostRequest.getUserTagIDList().isEmpty())) {
            return "Bài đăng phải có nội dung hoặc hình ảnh hoặc gắn thẻ người dùng!";
        }
        if (createNewPostRequest.getUserTagIDList() != null && createNewPostRequest.getUserTagIDList().contains(user.getPhoneNumber()))
            return "Không thể gắn thẻ chính bản thân vào bài viết!";
        if (createNewPostRequest.getAudience() != null && Audience.findByName(createNewPostRequest.getAudience()) == null)
            return "Quyền riêng tư bài viết không hợp lệ!";
        return "Hợp lệ!";
    }
    @Override
    public String createNewPost(Long userId, CreateNewPostRequest createNewPostRequest){
        try{
            User user = userRepository.findById(userId);
            String validate = validateCreateNewPostRequest(user,createNewPostRequest);
            if (!validate.equals("Hợp lệ!"))
                return validate;
            List<Resource> resourceList = new ArrayList<>();
            if (createNewPostRequest.getFiles() != null && !createNewPostRequest.getFiles()[0].isEmpty()) {
                resourceList = Arrays.stream(createNewPostRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image )).collect(Collectors.toList());
            }
            Post post = new Post(new Date(new Date().getTime() + 7 * 60 * 60*1000), new Date(new Date().getTime() + 7 * 60 * 60*1000), createNewPostRequest.getContent(),createNewPostRequest.getAudience() != null ? Audience.findByName(createNewPostRequest.getAudience()) : Audience.AllFriend,user, resourceList);
            if (createNewPostRequest.getUserTagIDList() != null && !createNewPostRequest.getUserTagIDList().isEmpty()) {
                List<PostUser> postUserList = createNewPostRequest.getUserTagIDList().stream().map(p -> new PostUser(post, userRepository.findByPhoneNumber(p), PostUserType.TagUser)).collect(Collectors.toList());
                post.setPostUserList(postUserList);
            }
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
            Post post = new Post();
            if (postRepository.findById(postId).isPresent())
                post = postRepository.findById(postId).get();
            else throw new Exception("Bài viết không tồn tại!");
            if (!post.getUser().getId().equals(userId)) throw new Exception("Bạn không có quyền chỉnh sửa bài viết này! ");
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
            System.out.println(createNewPostRequest.toString());
            String validate = validateCreateNewPostRequest(userRepository.findById(userId),createNewPostRequest);
            if (!validate.equals("Hợp lệ!"))
                return validate;
            Post post = postRepository.findByIdAndUser_Id(postId, userId);
            if (post == null || post.getUser().getId() != userId) throw new Exception("Bạn không có quyền chỉnh sửa bài viết này hoặc bài viết không còn tồn tại nữa!");
            if (createNewPostRequest.getContent() != null) post.setContentPost(createNewPostRequest.getContent());
            if (createNewPostRequest.getAudience() != null) post.setAudienceValue(Audience.findByName(createNewPostRequest.getAudience()));
            if (createNewPostRequest.getUserTagIDList() != null) {
                for (PostUser postUser : post.getPostUserList().stream().filter(postUser -> postUser.getPostUserType().equals(PostUserType.TagUser)).collect(Collectors.toList())){
                    post.getPostUserList().remove(postUser);
                }
                List<PostUser> postUserList = post.getPostUserList();
                postUserList.addAll(createNewPostRequest.getUserTagIDList().stream().map(p -> new PostUser(post, userRepository.findByPhoneNumber(p), PostUserType.TagUser)).collect(Collectors.toList()));
                post.setPostUserList(postUserList);
            }
            post.setUpdatedAt(new Date(new Date().getTime() + 7*60*60*1000));
            if (createNewPostRequest.getFiles() != null) {
                List<Resource> resourceList = Arrays.stream(createNewPostRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)).collect(Collectors.toList());
                post.setResourceList(resourceList);
            }
            postRepository.save(post);
            return "Chỉnh sửa bài viết thành công!";
        } catch(Exception e){
            System.out.println(e.toString());
            return e.getMessage();
        }
    }

    @Override
    public GetAllInfoPostUser getAllPostUser(Long userId){
        try{
            return new GetAllInfoPostUser("Thành công!",postRepository.findByUser_Id(userId).stream().map(p -> {
                GetInfoPostResponse getInfoPostResponse = modelMapper.map(p, GetInfoPostResponse.class);
                System.out.println(getInfoPostResponse.toString());
                getInfoPostResponse.setPostFather( p.getPostTop() != null ? modelMapper.map(p.getPostTop(), GetInfoPostResponse.class) : null);
                getInfoPostResponse.setUserLikeList(p.getPostUserList().stream().filter(postUser -> postUser.getPostUserType().equals(PostUserType.UserLike)).map(postUser -> new InfoUser(postUser.getUser().getFullName(), (postUser.getUser().getImageAvatarUrl() != null && !postUser.getUser().getImageAvatarUrl().isEmpty()) ? "http://localhost:8181/media/getImage/".concat(postUser.getUser().getImageAvatarUrl()) : null, postUser.getUser().getPhoneNumber())).collect(Collectors.toList()));
                getInfoPostResponse.setUserTagList(p.getPostUserList().stream().filter(postUser -> postUser.getPostUserType().equals(PostUserType.TagUser)).map(postUser -> new InfoUser(postUser.getUser().getFullName(), (postUser.getUser().getImageAvatarUrl() != null && !postUser.getUser().getImageAvatarUrl().isEmpty()) ? "http://localhost:8181/media/getImage/".concat(postUser.getUser().getImageAvatarUrl()) : null, postUser.getUser().getPhoneNumber())).collect(Collectors.toList()));
                getInfoPostResponse.setUserShareList(p.getPostTopList().stream().map(postUser -> new InfoUser(postUser.getUser().getFullName(),postUser.getUser().getImageAvatarUrl(), postUser.getUser().getPhoneNumber())).collect(Collectors.toList()));
                getInfoPostResponse.setFiles(p.getResourceList().stream().map(file -> "http://localhost:8181/media/" + (file.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/")  + file.getResourceValue()).collect(Collectors.toList()));
                getInfoPostResponse.setCreatedAt(p.getCreatedAt());
                getInfoPostResponse.setUpdatedAt(p.getUpdatedAt());
                return getInfoPostResponse;
            }).collect(Collectors.toList()));
        } catch(Exception e){
            System.out.println(e.toString());
            return new GetAllInfoPostUser("Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!", null);
        }
    }

    @Override
    public String likePost(Long postId, Long userId){
        try{
            Post post = new Post();
            if (postRepository.findById(postId).isPresent())
                post = postRepository.findById(postId).get();
            else return "Bài viết không tồn tại!";
            User user = userRepository.findById(userId);
            if (user == null) return "Người dùng hoặc bài viết không tồn tại!";
            PostUser postUser = new PostUser();
            if (!postUserRepository.existsByPostAndUserAndPostUserType(post,user,PostUserType.UserLike)){
                postUser = new PostUser(post,user, PostUserType.UserLike);
                postUserRepository.save(postUser);
                return "Thích bài viết thành công!";
            }
            else {
                if (postUserRepository.findFirstByPostAndUserAndPostUserTypeOrderByCreatedAtDesc(post,user,PostUserType.UserLike).getPostUserType() == PostUserType.UserLike)
                    postUser = new PostUser(post,user,PostUserType.UserDisLike);
                else
                    postUser = new PostUser(post,user,PostUserType.UserLike);
                postUserRepository.save(postUser);
                return "Cập nhật trạng thái thành công!";
            }

        } catch (Exception e){
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    // Hiển thị bài đăng
//    @Override
//    public

    @Override
    public String createNewComment(Long postId, Long userId, Long topComment, String content, MultipartFile file){
        try{
            Post post = new Post();
            if (postRepository.findById(postId).isPresent())
                post = postRepository.findById(postId).get();
            else return "Bài viết không tồn tại!";
            User user = userRepository.findById(userId);
            if (user == null) return "Người dùng không tồn tại!";
            Comment commentTop = commentRepository.findById(topComment).isPresent() ? commentRepository.findById(topComment).get() : null;
            if (topComment != null && commentTop == null) return "Bình luận không còn tồn tại hoặc người dùng không có quyền này!";
            if (isUserAuthorizeInteractPost(postId,userId)) {
                Comment comment = new Comment(content, post, commentTop, user, null, (file != null && !file.isEmpty()) ? new Resource(fileStorageService.storeFile(file), file.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image) : null);
                commentRepository.save(comment);
            }
            else return "Người dùng không có quyền thực hiện thao tác này!";
            return "Đăng bình luận thành công!";
        } catch (Exception e){
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String updateComment(Long userId, Long commentId, String content, MultipartFile file){
        try{
            Comment comment = commentRepository.findById(commentId).isPresent() ? commentRepository.findById(commentId).get() : null;
            if (comment == null) return "Bình luận không còn tồn tại hoặc người dùng không có quyền này!";
            if ( (content == null || content.isEmpty()) && (file == null || file.isEmpty()) )
                return "Bài viết không được để trống!";
            if (isUserAuthorizeInteractPost(comment.getPost().getId(),userId)){
                if (content != null){
                    comment.setContentComment(content);
                }
                if (file != null && !file.isEmpty()){
                    String fileName = fileStorageService.storeFile(file);
                    comment.setResource(new Resource(fileName, file.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)) ;
                }
                commentRepository.save(comment);
                return "Cập nhật bình luận thành công!";
            }
            return "Người dùng không có quyền thực hiện hành động này!";
        } catch (Exception e){
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String deleteComment(Long commentId, Long userId){
        try{
            Comment comment = new Comment();
            if (commentRepository.findById(commentId).isPresent())
                comment = commentRepository.findById(commentId).get();
            else return "Bình luận không tồn tại!";
            if (!comment.getUser().getId().equals(userId)) return "Người dùng không có quyền xóa bình luận này!";
            comment.setIsDelete(true);
            comment.setUpdatedAt(new Date(new Date().getTime() + 7*60*60*1000));
            commentRepository.save(comment);
            return "Xóa bình luận thành công!";
        } catch (Exception e){
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String likeComment(Long userId, Long commentId){
        try{
            if (!userRepository.existsUserById(userId) || !commentRepository.existsCommentById(commentId) || isUserAuthorizeInteractComment(commentId,userId)) return "Không tồn tại hoặc bạn không được phép thực hiện hành động này!";
            User user = userRepository.findById(userId);
            Comment comment = commentRepository.findById(commentId).get();
            CommentUser commentUser = new CommentUser();
            if (commentUserRepository.findFirstByUserAndCommentOrderByCreatedAtDesc(user,comment).getIsUserLike()){
                commentUser = new CommentUser(comment,user,false);
            }
            else
                commentUser = new CommentUser(comment,user,true);
            commentUserRepository.save(commentUser);
            return "Cập nhật trạng thái thành công!";
        } catch (Exception e){
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    // Thiếu kiểm tra quyền user
    @Override
    public Long getAmountComment(Long postId, Long userId) throws Exception{
        if (!postRepository.existsById(postId)) throw new Exception("Bài viết này không tồn tại hoặc người dùng không có quyền xem bài viết này!");
        return (long) postRepository.findById(postId).get().getCommentList().size();
    }

    // Thiếu kiểm tra quyền user
    @Override
    public List<InfoComment> getAllInfoComment(Long postId, Long userId) throws Exception{
        if (!postRepository.existsById(postId)) throw new Exception("Bài viết này không tồn tại hoặc người dùng không có quyền xem bài viết này!");
        return postRepository.findById(postId).get().getCommentList().stream().map(comment -> {
            try {
                return getInfoComment(comment.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public InfoComment getInfoComment(Long commentId) throws Exception {
        try{
            if (!commentRepository.existsCommentById(commentId)) throw new Exception("Bình luận này không tồn tại hoặc người dùng không có quyền xem bình luận này!");
            Comment comment = commentRepository.findById(commentId).get();
            InfoUser infoUser = new InfoUser(comment.getUser().getFullName(), comment.getUser().getImageAvatarUrl(), comment.getUser().getPhoneNumber());
            Set<User> userSet = new HashSet<>();
            for(CommentUser commentUser : comment.getCommentUserList()){
                userSet.add(commentUser.getUser());
            }
            List<InfoUser> infoUserList = new ArrayList<>();
            for (User user : userSet){
                if (commentUserRepository.findFirstByUserAndCommentOrderByCreatedAtDesc(user,comment).getIsUserLike()){
                    infoUserList.add(new InfoUser(user.getFullName(),user.getImageAvatarUrl(),user.getPhoneNumber()));
                }
            }
            List<InfoComment> infoCommentList = comment.getCommentBot().stream().map(commentBot -> {
                try {
                    return getInfoComment(commentBot.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            return new InfoComment(infoUser,infoUserList,infoCommentList,comment.getCreatedAt(),comment.getUpdatedAt());
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    // Chưa viết
    public Boolean isUserAuthorizeInteractPost(Long postId, Long userId){
        if (postRepository.findByIdAndUser_Id(postId, userId) == null) return false;

        return true;
    }

    public Boolean isUserAuthorizeInteractComment(Long commentId, Long userId){
        return true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllInfoPostUser {
        private String mesage;
        private List<GetInfoPostResponse> getInfoPostResponse;
    }

}
