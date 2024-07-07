package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.payload.response.Admin.GeneralInfoResponse;
import com.essay.zaloapp.domain.payload.response.Admin.GroupsResponse;
import com.essay.zaloapp.domain.payload.response.Admin.PostsResponse;
import com.essay.zaloapp.domain.payload.response.Admin.UsersResponse;

import java.util.List;

public interface AdminService {
    List<UsersResponse> getAllUsersForAdmin();

    List<PostsResponse> getAllPostsForAdmin();

    List<GroupsResponse> getAllGroupsForAdmin();

    GeneralInfoResponse getGeneralInfo();

    String deleteGroupChatForAdmin(Long groupId);

    String deletePostForAdmin(Long postId);

    String lockUserForAdmin(Long userId);
}
