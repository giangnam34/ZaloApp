package com.essay.zaloapp.domain.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    private Date updatedAt;

    private Boolean isDelete;

    // Nội dung comment
    private String contentComment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_top_id")
    private Comment commentTop;

    @OneToMany(mappedBy = "commentTop", cascade = CascadeType.ALL)
    private List<Comment> commentBot;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Danh sách người dùng like bình luận
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentUser> commentUserList;

    @OneToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    public Comment(String contentComment, Post post, Comment commentTop, User user, List<CommentUser> commentUserList, Resource resource) {
        this.contentComment = contentComment;
        this.post = post;
        this.commentTop = commentTop;
        this.user = user;
        this.commentUserList = commentUserList;
        this.resource = resource;
        this.createdAt = new Date( new Date().getTime() + 7 * 60 * 60*1000);
        this.isDelete = false;
    }

    @PreUpdate
    public void updateTime(){
        this.updatedAt = new Date(new Date().getTime() + 7*60*60*1000);
    }
}
