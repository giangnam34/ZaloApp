package com.essay.zaloapp.domain.payload.response.SocialMedia;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.payload.response.findUserByPhoneNumberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInfoPostResponse {

    private Long id;

    //Nội dung bài viết
    private String content;

    // Đối tượng bài viết: Tất cả bạn bè, Chỉ mình tôi, Tất Cả trừ một số người, Chỉ một số người được coi
    private Audience audience;

    private GetInfoPostResponse postFather;

    // Danh sách bạn bè được gắn thẻ hoặc được chỉ định nhìn thầy/không thấy bài viết
    private List<findUserByPhoneNumberResponse> userTagList;

    private List<findUserByPhoneNumberResponse> userLikeList;

    private List<findUserByPhoneNumberResponse> userShareList;

    // Hình ảnh hoặc video kèm theo
    private MultipartFile[] files;
}
