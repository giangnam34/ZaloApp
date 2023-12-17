package com.essay.zaloapp.domain.payload.response.SocialMedia;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.payload.response.Authorize.InfoUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetInfoPostResponse {

    private Long id;

    //Nội dung bài viết
    private String content;

    // Đối tượng bài viết: Tất cả bạn bè, Chỉ mình tôi, Tất Cả trừ một số người, Chỉ một số người được coi
    private Audience audience;

    private GetInfoPostResponse postFather;

    // Danh sách bạn bè được gắn thẻ hoặc được chỉ định nhìn thầy/không thấy bài viết
    private List<InfoUser> userTagList;

    private List<InfoUser> userLikeList;

    private List<InfoUser> userShareList;

    // Hình ảnh hoặc video kèm theo
    private List<String> files;

    private Date createdAt;

    private Date updatedAt;
}
