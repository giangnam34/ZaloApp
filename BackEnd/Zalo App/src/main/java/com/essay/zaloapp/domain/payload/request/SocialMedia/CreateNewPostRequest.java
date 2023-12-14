package com.essay.zaloapp.domain.payload.request.SocialMedia;

import com.essay.zaloapp.domain.enums.Audience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateNewPostRequest {

    // Nội dung bài viết
    private String content;

    // Đối tượng bài viết: Tất cả bạn bè, Chỉ mình tôi, Tất Cả trừ một số người, Chỉ một số người được coi
    private Audience audience;

    // Danh sách bạn bè được gắn thẻ hoặc được chỉ định nhìn thầy/không thấy bài viết
    private List<String> userTagIDList;

    // Hình ảnh hoặc video kèm theo
    private MultipartFile[] files;


}
