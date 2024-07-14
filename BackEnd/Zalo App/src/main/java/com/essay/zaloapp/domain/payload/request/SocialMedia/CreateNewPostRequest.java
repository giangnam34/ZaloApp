package com.essay.zaloapp.domain.payload.request.SocialMedia;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
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
