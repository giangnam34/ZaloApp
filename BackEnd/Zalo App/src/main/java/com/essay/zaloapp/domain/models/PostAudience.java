package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.Audience;

import javax.persistence.*;

@Entity
// Đối tượng bài viết: Tất cả bạn bè, Chỉ mình tôi, Tất Cả trừ một số người, Chỉ một số người được coi
public class PostAudience {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private Audience value;


}
