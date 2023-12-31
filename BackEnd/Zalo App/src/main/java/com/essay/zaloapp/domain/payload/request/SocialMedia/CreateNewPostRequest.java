package com.essay.zaloapp.domain.payload.request.SocialMedia;

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

    // Id bài viết gốc, dùng để chia sẻ bài viết
    private Long postTopId;

    // Nội dung bài viết
    private String content;

    // Đối tượng bài viết: Tất cả bạn bè, Chỉ mình tôi, Tất Cả trừ một số người, Chỉ một số người được coi
    private String audience;

    // Danh sách bạn bè được gắn thẻ hoặc được chỉ định nhìn thầy/không thấy bài viết
    private List<String> userTagIDList;

    // Hình ảnh hoặc video kèm theo
    private MultipartFile[] files;


}
