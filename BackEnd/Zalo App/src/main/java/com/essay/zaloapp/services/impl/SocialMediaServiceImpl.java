package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.enums.ResourceType;
import com.essay.zaloapp.domain.models.*;
import com.essay.zaloapp.domain.models.Composite.FriendsId;
import com.essay.zaloapp.domain.payload.request.SocialMedia.CreateNewPostRequest;
import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import com.essay.zaloapp.domain.payload.response.SocialMedia.Comment.InfoComment;
import com.essay.zaloapp.domain.payload.response.SocialMedia.GetAllUsersLikedPostResponse;
import com.essay.zaloapp.domain.payload.response.SocialMedia.GetInfoPostResponse;
import com.essay.zaloapp.repository.*;
import com.essay.zaloapp.services.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.poi.ss.formula.functions.T;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private FormatDate formatDate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostUserRepository postUserRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentUserRepository commentUserRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private RecommendSystemService recommendSystemService;

    @Autowired
    private PreferenceCalculator preferenceCalculator;

    @Override
    public String validateCreateNewPostRequest(User user, CreateNewPostRequest createNewPostRequest) {
        if (createNewPostRequest.getPostTopId() == null && (createNewPostRequest.getFiles() == null || (createNewPostRequest.getFiles() != null && createNewPostRequest.getFiles()[0].isEmpty())) && (createNewPostRequest.getContent() == null || createNewPostRequest.getContent().isEmpty()) && (createNewPostRequest.getUserTagIDList() == null || createNewPostRequest.getUserTagIDList().isEmpty())) {
            return "Bài đăng phải có nội dung hoặc hình ảnh hoặc gắn thẻ người dùng!";
        }
        if (createNewPostRequest.getUserTagIDList() != null && createNewPostRequest.getUserTagIDList().contains(user.getPhoneNumber()))
            return "Không thể gắn thẻ chính bản thân vào bài viết!";
        if (createNewPostRequest.getAudience() != null && Audience.findByName(createNewPostRequest.getAudience()) == null)
            return "Quyền riêng tư bài viết không hợp lệ!";
        return "Hợp lệ!";
    }

    @Override
    public String createNewPost(Long userId, CreateNewPostRequest createNewPostRequest) {
        try {
            User user = userRepository.findById(userId);
            String validate = validateCreateNewPostRequest(user,createNewPostRequest);
            Long sharePostID = null;
            if (!validate.equals("Hợp lệ!"))
                return validate;
            List<Resource> resourceList = new ArrayList<>();
            if (createNewPostRequest.getFiles() != null && !createNewPostRequest.getFiles()[0].isEmpty()) {
                resourceList = Arrays.stream(createNewPostRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)).collect(Collectors.toList());
            }
            Post post = new Post(new Date(new Date().getTime()), new Date(new Date().getTime()), createNewPostRequest.getContent(), createNewPostRequest.getAudience() != null ? Audience.findByName(createNewPostRequest.getAudience()) : Audience.Public, user, resourceList);
            if (createNewPostRequest.getPostTopId() != null) {
                Post postTop = postRepository.findById(createNewPostRequest.getPostTopId()).get();
                if (postTop == null) return "Bài viết chia sẻ đã không còn tồn tại!";
                if (!isUserAuthorizeInteractPost(post, user)) return "Người dùng không có quyền chia sẻ bài viết này!";
                post.setPostTop(postTop);
                sharePostID = postTop.getId();
                postTop.setPostScore( (postTop.getPostScore() != null ? postTop.getPostScore() : 0.000000000000000000) + 0.600000000000000000);
            }
            if (createNewPostRequest.getUserTagIDList() != null && !createNewPostRequest.getUserTagIDList().isEmpty()) {
                for (String userPhoneNumber : createNewPostRequest.getUserTagIDList()) {
                    if (!userRepository.existsUserByPhoneNumber(userPhoneNumber))
                        return "Gắn thẻ người dùng không hợp lệ";
                }
                List<PostUser> postUserList = createNewPostRequest.getUserTagIDList().stream().map(p -> new PostUser(post, userRepository.findByPhoneNumber(p), PostUserType.TagUser)).collect(Collectors.toList());
                post.setPostUserList(postUserList);
            }
            postRepository.save(post);
            if (sharePostID != null){

                preferenceCalculator.updatePreferenceUser(user,sharePostID, 0L, 0L, 1L);
            }
            return "Đăng bài viết thành công!";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "Có lỗi trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String updateAudiencePost(Long userId, Long postId, Audience audience) {
        try {
            Post post = new Post();
            if (postRepository.findById(postId).isPresent())
                post = postRepository.findById(postId).get();
            else throw new Exception("Bài viết không tồn tại!");
            if (!post.getUser().getId().equals(userId))
                throw new Exception("Bạn không có quyền chỉnh sửa bài viết này! ");
            post.setAudienceValue(audience);
            postRepository.save(post);
            return "Đổi đối tượng bài viết thành công!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String updatePost(Long userId, Long postId, CreateNewPostRequest createNewPostRequest) {
        try {
            String validate = validateCreateNewPostRequest(userRepository.findById(userId), createNewPostRequest);
            if (!validate.equals("Hợp lệ!"))
                return validate;
            Post post = postRepository.findByIdAndUser_Id(postId, userId);
            if (post == null || post.getUser().getId() != userId)
                throw new Exception("Bạn không có quyền chỉnh sửa bài viết này hoặc bài viết không còn tồn tại nữa!");
            if (createNewPostRequest.getContent() != null) post.setContentPost(createNewPostRequest.getContent());
            if (createNewPostRequest.getAudience() != null)
                post.setAudienceValue(Audience.findByName(createNewPostRequest.getAudience()));
            if (createNewPostRequest.getUserTagIDList() != null) {
                for (PostUser postUser : post.getPostUserList().stream().filter(postUser -> postUser.getPostUserType().equals(PostUserType.TagUser)).collect(Collectors.toList())) {
                    post.getPostUserList().remove(postUser);
                }
                List<PostUser> postUserList = post.getPostUserList();
                postUserList.addAll(createNewPostRequest.getUserTagIDList().stream().map(p -> new PostUser(post, userRepository.findByPhoneNumber(p), PostUserType.TagUser)).collect(Collectors.toList()));
                post.setPostUserList(postUserList);
            }
            post.setUpdatedAt(new Date(new Date().getTime() + 7 * 60 * 60 * 1000));
            if (createNewPostRequest.getFiles() != null) {
                if (createNewPostRequest.getFiles()[0].isEmpty()) post.setResourceList(new ArrayList<>());
                else {
                    List<Resource> resourceList = Arrays.stream(createNewPostRequest.getFiles()).map(p -> new Resource(fileStorageService.storeFile(p), p.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image)).collect(Collectors.toList());
                    post.setResourceList(resourceList);
                }
            }
            postRepository.save(post);
            return "Chỉnh sửa bài viết thành công!";
        } catch (Exception e) {
            System.out.println(e.toString());
            return e.getMessage();
        }
    }

    @Override
    public String deletePost(Long userId, Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(Exception::new);
            if (post.getUser().getId() != userId)
                return "Người dùng không có quyền thực hiện hành động này!";
            if (post.getPostTop() != null){
                Post postTop = post.getPostTop();
                if (postTop.getPostScore() != null && postTop.getPostScore() - 0.600000000000000000 >= 0.000000000000000000) {
                    postTop.setPostScore(postTop.getPostScore() - 0.600000000000000000);
                    postRepository.save(postTop);
                }
            }
            postRepository.delete(post);
            return "Xóa bài viết thành công!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra. Vui lòng thử lại!";
        }
    }

    // Nhận tất cả bài viết của người dùng
    @Override
    public GetAllInfoPostUser getAllPostUser(Long userId, Long page){
        try{
            Pageable pageable = PageRequest.of(page.intValue(),10);
            return new GetAllInfoPostUser("Thành công!", mapListPostEntityToResponse(postRepository.findByUser_Id(userId, pageable)));
        } catch(Exception e){
            System.out.println(e.toString());
            return new GetAllInfoPostUser("Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!", null);
        }
    }

    // Nhận bảng tin của người dùng
    @Override
    public GetAllInfoPostUser getNewFeedUser(Long userId, Long page) throws IOException, TasteException {
        User user = userRepository.findById(userId);
        Long age = (new Date().getTime() - user.getBirthDay().getTime())/(1000l * 60 * 60 * 24 * 365);
        List<RecommendedItem> recommendIDList = recommendSystemService.recommendSystem(userId,age);
        List<Post> recommendedPostList = recommendIDList.stream().map(recommendElement -> postRepository.findById(recommendElement.getItemID()).get()).toList();
        List <Post> postList =  recommendedPostList.stream().filter(post -> {
            return (post.getAudienceValue() == Audience.Public ||
                    Objects.equals(post.getUser().getId(), userId) ||
                    (friendService.isFriendUser(userId,post.getUser().getId()) &&
                            (post.getAudienceValue() == Audience.AllFriend || (post.getAudienceValue() == Audience.SomeOneCanSee && !post.getPostUserList().stream().filter(postUser -> postUser.getPostUserType() == PostUserType.TagUser && postUser.getUser().getId() == userId).collect(Collectors.toList()).isEmpty())
                                    || (post.getAudienceValue() == Audience.AllExceptSomeOne && post.getPostUserList().stream().filter(postUser -> postUser.getPostUserType() == PostUserType.TagUser && postUser.getUser().getId() == userId).collect(Collectors.toList()).isEmpty()))));
        }).collect(Collectors.toList());
        if (postList.isEmpty()){
            postList = postRepository.findAll(PageRequest.of(1,10)).stream().filter(post -> {
                return (post.getAudienceValue() == Audience.Public ||
                        post.getUser().getId() == userId ||
                        (friendService.isFriendUser(userId,post.getUser().getId()) &&
                                (post.getAudienceValue() == Audience.AllFriend || (post.getAudienceValue() == Audience.SomeOneCanSee && !post.getPostUserList().stream().filter(postUser -> postUser.getPostUserType() == PostUserType.TagUser && postUser.getUser().getId() == userId).collect(Collectors.toList()).isEmpty())
                                        || (post.getAudienceValue() == Audience.AllExceptSomeOne && post.getPostUserList().stream().filter(postUser -> postUser.getPostUserType() == PostUserType.TagUser && postUser.getUser().getId() == userId).collect(Collectors.toList()).isEmpty()))));
            }).collect(Collectors.toList());
        }
        return new GetAllInfoPostUser("Thành công!", mapListPostEntityToResponse(postList));
    }

    @Override
    public List<GetInfoPostResponse> mapListPostEntityToResponse(List<Post> postList) {
        return postList.stream().map(p -> {
            return mapPostEntityToResponse(p);
        }).collect(Collectors.toList());
    }

    @Override
    public GetInfoPostResponse mapPostEntityToResponse(Post post) {
        GetInfoPostResponse getInfoPostResponse = modelMapper.map(post, GetInfoPostResponse.class);
        System.out.println(getInfoPostResponse.toString());
        getInfoPostResponse.setPostFather(post.getPostTop() != null ? mapPostEntityToResponse(post.getPostTop()) : null);
        List<User> userLikeList = new ArrayList<>();
        List<User> userTagList = new ArrayList<>();
        Map<User, PostUserType> postUserTypeMap = new HashMap<>();
        for (PostUser postUser : post.getPostUserList()) {
            postUserTypeMap.put(postUser.getUser(), postUser.getPostUserType());
        }
        for (Map.Entry<User, PostUserType> key : postUserTypeMap.entrySet()) {
            if (key.getValue().equals(PostUserType.UserLike)) userLikeList.add(key.getKey());
            else if (key.getValue().equals(PostUserType.TagUser)) userTagList.add(key.getKey());
        }
        getInfoPostResponse.setUserLikeList(userLikeList.stream().map(user -> new InfoUser(user.getFullName(), (user.getImageAvatarUrl() != null && !user.getImageAvatarUrl().isEmpty()) ? "http://localhost:8181/media/getImage/".concat(user.getImageAvatarUrl()) : null, user.getPhoneNumber(), post.getUser().getStatus().name())).collect(Collectors.toList()));
        getInfoPostResponse.setUserTagList(userTagList.stream().map(user -> new InfoUser(user.getFullName(), (user.getImageAvatarUrl() != null && !user.getImageAvatarUrl().isEmpty()) ? "http://localhost:8181/media/getImage/".concat(user.getImageAvatarUrl()) : null, user.getPhoneNumber(), post.getUser().getStatus().name())).collect(Collectors.toList()));
        getInfoPostResponse.setUserShareList(post.getPostTopList().stream().map(postUser -> new InfoUser(postUser.getUser().getFullName(), postUser.getUser().getImageAvatarUrl(), postUser.getUser().getPhoneNumber(), post.getUser().getStatus().name())).collect(Collectors.toList()));
        getInfoPostResponse.setFiles(post.getResourceList().stream().map(file -> "http://localhost:8181/media/" + (file.getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + file.getResourceValue()).collect(Collectors.toList()));
        getInfoPostResponse.setCreatedAt(formatDate.formatDate(post.getCreatedAt()));
        getInfoPostResponse.setUpdatedAt(formatDate.formatDate(post.getUpdatedAt()));
        getInfoPostResponse.setUserPost(new InfoUser(post.getUser().getFullName(), "http://localhost:8181/media/getImage/" + post.getUser().getImageAvatarUrl(), post.getUser().getPhoneNumber(), post.getUser().getStatus().name()));
        long nonDeletedCommentCount = post.getCommentList().stream()
                .filter(comment -> comment.getIsDelete() != true)
                .count();
        getInfoPostResponse.setCommentCount(Long.valueOf(nonDeletedCommentCount));
        return getInfoPostResponse;
    }

    @Override
    public String likePost(Long postId, Long userId) {
        try {
            Post post = new Post();
            if (postRepository.findById(postId).isPresent())
                post = postRepository.findById(postId).get();
            else return "Bài viết không tồn tại!";
            User user = userRepository.findById(userId);
            if (user == null) return "Người dùng hoặc bài viết không tồn tại!";
            if (!isUserAuthorizeInteractPost(post, user)) return "Người dùng không có quyền thực hiện hành động này!";
            Map<User, PostUserType> postUserTypeMap = new HashMap<>();
            for (PostUser postUser : post.getPostUserList()) {
                postUserTypeMap.put(postUser.getUser(), postUser.getPostUserType());
            }
            PostUser postUser = new PostUser();
            if (!postUserTypeMap.containsKey(user) || (postUserTypeMap.get(user).equals(PostUserType.UserDisLike))){
                postUser = new PostUser(post,user, PostUserType.UserLike);
                post.setPostScore( (post.getPostScore() != null ? post.getPostScore() : 0.000000000000000000) + 0.100000000000000000);
                postUserRepository.save(postUser);
                preferenceCalculator.updatePreferenceUser(user,post.getId(),1L,0L,0L);
                return "Thích bài viết thành công!";
            }
            else {
                postUser = new PostUser(post,user,PostUserType.UserDisLike);
                if (post.getPostScore() != null && post.getPostScore() - 0.100000000000000000 >= 0.000000000000000000) post.setPostScore( post.getPostScore() - 0.100000000000000000);
                postUserRepository.save(postUser);
                preferenceCalculator.updatePreferenceUser(user,post.getId(),-1L,0L,0L);
                return "Hủy thích bài viết thành công!";
            }

        } catch (Exception e) {
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String createNewComment(Long postId, Long userId, Long topComment, String content, MultipartFile file) {
        try {
            Post post = new Post();
            if (postRepository.findById(postId).isPresent())
                post = postRepository.findById(postId).get();
            else return "Bài viết không tồn tại!";
            User user = userRepository.findById(userId);
            if (user == null) return "Người dùng không tồn tại!";
            Comment commentTop = new Comment();
            if (topComment != null) {
                commentTop = commentRepository.findById(topComment).isPresent() ? commentRepository.findById(topComment).get() : null;
                if (commentTop == null) return "Bình luận không còn tồn tại hoặc người dùng không có quyền này!";
            } else commentTop = null;
            if (isUserAuthorizeInteractPost(post, user)) {
                Comment comment = new Comment(content, post, commentTop, user, null, (file != null && !file.isEmpty()) ? new Resource(fileStorageService.storeFile(file), file.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image) : null);
                post.setPostScore( (post.getPostScore() != null ? post.getPostScore() : 0.000000000000000000) + 0.300000000000000000);
                commentRepository.save(comment);
                preferenceCalculator.updatePreferenceUser(user,post.getId(), 0L, 1L, 0L);
            }
            else return "Người dùng không có quyền thực hiện thao tác này!";
            return "Đăng bình luận thành công!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String updateComment(Long userId, Long commentId, String content, Object file) {
        try {
            Comment comment = null;
            if (commentId != null) {
                comment = commentRepository.findById(commentId).orElse(null);
                if (comment == null) {
                    return "Bình luận không còn tồn tại hoặc người dùng không có quyền này!";
                }
            }

            User user = userRepository.findById(userId);
            if (user == null) {
                return "Người dùng không tồn tại!";
            }

            if ((content == null || content.isEmpty()) && (file == null || (file instanceof MultipartFile && ((MultipartFile) file).isEmpty()))) {
                return "Không có nội dung hoặc file để cập nhật!";
            }

            if (isUserAuthorizeInteractComment(comment, user)) {
                if (content != null) {
                    comment.setContentComment(content);
                }

                if (file != null) {
                    if (file instanceof MultipartFile) {
                        // Handle MultipartFile case
                        MultipartFile multipartFile = (MultipartFile) file;
                        if (!multipartFile.isEmpty()) {
                            String fileName = fileStorageService.storeFile(multipartFile);
                            comment.setResource(new Resource(fileName, multipartFile.getContentType().contains("video") ? ResourceType.Video : ResourceType.Image));
                        } else {
                            if (comment.getResource() != null) {
                                comment.setResource(null);
                            }
                        }
                    }
                }
                commentRepository.save(comment);
                return "Cập nhật bình luận thành công!";
            }

            return "Người dùng không có quyền thực hiện hành động này!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String deleteComment(Long commentId, Long userId) {
        try {
            Comment comment = new Comment();
            if (commentId != null) {
                comment = commentRepository.findById(commentId).isPresent() ? commentRepository.findById(commentId).get() : null;
                if (comment == null) return "Bình luận không còn tồn tại hoặc người dùng không có quyền này!";
            }
            if (!comment.getUser().getId().equals(userId)) return "Người dùng không có quyền xóa bình luận này!";
            comment.setIsDelete(true);
            //comment.setUpdatedAt(new Date(new Date().getTime() + 7*60*60*1000));
            Post post = comment.getPost();
            if (post.getPostScore() != null && post.getPostScore() - 0.3000000000000000000 >= 0.0000000000000000000){
                post.setPostScore(post.getPostScore() - 0.3000000000000000000);
            }
            commentRepository.save(comment);
            preferenceCalculator.updatePreferenceUser(comment.getUser(), comment.getPost().getId(), 0L, -1L, 0L);
            return "Xóa bình luận thành công!";
        } catch (Exception e) {
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public String likeComment(Long userId, Long commentId) {
        try {
            String result = "";
            if (!userRepository.existsUserById(userId) || !commentRepository.existsCommentById(commentId))
                return "Không tồn tại hoặc bạn không được phép thực hiện hành động này!";
            User user = userRepository.findById(userId);
            Comment comment = commentRepository.findById(commentId).get();
            CommentUser commentUser = new CommentUser();
            if (!isUserAuthorizeInteractComment(comment, user)) return "Bạn không có quyền thực hiện hành động này!";
            if (commentUserRepository.existsCommentUserByUserAndComment(user, comment) && commentUserRepository.findFirstByUserAndCommentOrderByCreatedAtDesc(user, comment).getIsUserLike()) {
                commentUser = new CommentUser(comment, user, false);
                result = "Hủy thích bình luận thành công!";
            } else {
                commentUser = new CommentUser(comment, user, true);
                result = "Thích bình luận thành công!";
            }
            commentUserRepository.save(commentUser);
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Có lỗi xảy ra trong quá trình thực thi. Vui lòng thử lại!";
        }
    }

    @Override
    public Long getAmountComment(Long postId, Long userId) throws Exception {
        if (!postRepository.existsById(postId) || !userRepository.existsUserById(userId) || !isUserAuthorizeInteractPost(postRepository.findById(postId).get(), userRepository.findById(userId)))
            throw new Exception("Bài viết này không tồn tại hoặc người dùng không có quyền xem bài viết này!");
        return (long) (int) postRepository.findById(postId).get().getCommentList().stream().filter(comment -> !comment.getIsDelete()).count();
    }

    @Override
    public List<InfoComment> getAllInfoComment(Long postId, Long userId) throws Exception {
        if (!postRepository.existsById(postId))
            throw new Exception("Bài viết này không tồn tại hoặc người dùng không có quyền xem bài viết này!");
        return postRepository.findById(postId).get().getCommentList().stream().filter(comment -> !comment.getIsDelete()).map(comment -> {
            try {
                return getInfoComment(comment.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public InfoComment getInfoComment(Long commentId) throws Exception {
        try {
            if (!commentRepository.existsCommentById(commentId))
                throw new Exception("Bình luận này không tồn tại hoặc người dùng không có quyền xem bình luận này!");
            Comment comment = commentRepository.findById(commentId).get();
            InfoUser infoUser = new InfoUser(comment.getUser().getFullName(), "http://localhost:8181/media/getImage/" + comment.getUser().getImageAvatarUrl(), comment.getUser().getPhoneNumber(), comment.getUser().getStatus().name());
            Set<User> userSet = new HashSet<>();
            for (CommentUser commentUser : comment.getCommentUserList()) {
                userSet.add(commentUser.getUser());
            }
            List<InfoUser> infoUserList = new ArrayList<>();
            for (User user : userSet) {
                if (commentUserRepository.findFirstByUserAndCommentOrderByCreatedAtDesc(user, comment).getIsUserLike()) {
                    infoUserList.add(new InfoUser(user.getFullName(), "http://localhost:8181/media/getImage/" + user.getImageAvatarUrl(), user.getPhoneNumber(), user.getStatus().name()));
                }
            }
            List<InfoComment> infoCommentList = comment.getCommentBot().stream().map(commentBot -> {
                try {
                    return getInfoComment(commentBot.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            return new InfoComment(commentId, infoUser, infoUserList, infoCommentList, comment.getContentComment(), comment.getResource() != null ? "http://localhost:8181/media/" + (comment.getResource().getResourceType().equals(ResourceType.Video) ? "getVideo/" : "getImage/") + comment.getResource().getResourceValue() : null, comment.getCreatedAt(), comment.getUpdatedAt());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Chưa viết
    public Boolean isUserAuthorizeInteractPost(Post post, User user) {
        if (!user.getIsConfirmed() || user.getIsLocked()) return false;
        if (post.getAudienceValue().equals(Audience.Public)) return true;
        if (!post.getUser().equals(user)) {
            if (!friendsRepository.existsFriendsByFriendsId(new FriendsId(Math.min(post.getUser().getId(), user.getId()), Math.max(post.getUser().getId(), user.getId()))))
                return false;
            if (friendsRepository.findByFriendsId(new FriendsId(Math.min(post.getUser().getId(), user.getId()), Math.max(post.getUser().getId(), user.getId()))).getIsBlock() > 0 ||
                    friendsRepository.findByFriendsId(new FriendsId(Math.min(post.getUser().getId(), user.getId()), Math.max(post.getUser().getId(), user.getId()))).getIsDelete() > 0)
                return false;
        }
        if (post.getAudienceValue().equals(Audience.OnlyMe) && !post.getUser().equals(user)) return false;
        if (post.getAudienceValue().equals(Audience.AllExceptSomeOne) && !post.getPostUserList().stream().filter(postUser -> (postUser.getUser().equals(user) && postUser.getPostUserType().equals(PostUserType.TagUser))).collect(Collectors.toList()).isEmpty())
            return false;
        if (post.getAudienceValue().equals(Audience.SomeOneCanSee) && post.getPostUserList().stream().filter(postUser -> (postUser.getUser().equals(user) && postUser.getPostUserType().equals(PostUserType.TagUser))).collect(Collectors.toList()).isEmpty())
            return false;
        return true;
    }

    public Boolean isUserAuthorizeInteractComment(Comment comment, User user) {
        if (!isUserAuthorizeInteractPost(comment.getPost(), user)) return false;
        if (!comment.getUser().equals(user)) {
            if (!friendsRepository.existsFriendsByFriendsId(new FriendsId(Math.min(comment.getUser().getId(), user.getId()), Math.max(comment.getUser().getId(), user.getId()))))
                return false;
            if (friendsRepository.findByFriendsId(new FriendsId(Math.min(comment.getUser().getId(), user.getId()), Math.max(comment.getUser().getId(), user.getId()))).getIsBlock() > 0 ||
                    friendsRepository.findByFriendsId(new FriendsId(Math.min(comment.getUser().getId(), user.getId()), Math.max(comment.getUser().getId(), user.getId()))).getIsDelete() > 0)
                return false;
        }
        return true;
    }

    @Override
    public GetAllUsersLikedPost getAllUsersLikedPost(Long postId) {
        List<GetAllUsersLikedPostResponse> getAllUsersLikedPostResponses = new ArrayList<>();
        try {
            Optional<Post> optionalPost = postRepository.findById(postId);
            if (!optionalPost.isPresent()) {
                return new GetAllUsersLikedPost("Post không tồn tại!", getAllUsersLikedPostResponses);
            }

            List<Long> interactedUserIds = postUserRepository.findUserIdsByPostId(postId);
            Set<Long> uniqueUserIds = new HashSet<>(interactedUserIds);
            List<Long> uniqueUserIdsList = new ArrayList<>(uniqueUserIds);

            for (Long userId : uniqueUserIdsList) {
                List<PostUser> postUserList = postUserRepository.findAllByPostIdAndUserId(postId, userId);
                int countLike = 0;
                int countDislike = 0;
                for (PostUser postUser : postUserList) {
                    if (postUser.getPostUserType().equals(PostUserType.UserLike)) {
                        countLike++;
                    }
                    if (postUser.getPostUserType().equals(PostUserType.UserDisLike)) {
                        countDislike++;
                    }
                }
                if (countLike > countDislike) {
                    GetAllUsersLikedPostResponse getAllUsersLikedPostResponse = new GetAllUsersLikedPostResponse();
                    User user = userRepository.findById(userId);
                    getAllUsersLikedPostResponse.setPhoneNumber(user.getPhoneNumber());
                    getAllUsersLikedPostResponse.setUserName(user.getFullName());
                    getAllUsersLikedPostResponse.setImageAvatar("http://localhost:8181/media/getImage/" + user.getImageAvatarUrl());
                    getAllUsersLikedPostResponses.add(getAllUsersLikedPostResponse);

                }
            }
            return new GetAllUsersLikedPost("Lấy danh sách người thích thành công!", getAllUsersLikedPostResponses);
        } catch (Exception e) {
            return new GetAllUsersLikedPost("Đã có lỗi xảy ra!", getAllUsersLikedPostResponses);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllInfoPostUser {
        private String mesage;
        private List<GetInfoPostResponse> getInfoPostResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAllUsersLikedPost {
        private String mesage;
        private List<GetAllUsersLikedPostResponse> getAllUsersLikedPostResponses;
    }
}