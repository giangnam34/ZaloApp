package com.essay.zaloapp.domain.payload.response.SocialMedia.Comment;

import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoComment {

    // Người bình luận comment này
    private InfoUser userComment;

    // Danh sách người like comment này
    private List<InfoUser> userLike;

    // Danh sách bình luận con của comment này
    private List<InfoComment> infoCommentList;

    // Ngày bình luận
    private Date createdAt;

    // Ngày cập nhật bình luận
    private Date updatedAt;


}
