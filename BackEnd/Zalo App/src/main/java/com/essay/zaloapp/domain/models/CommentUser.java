package com.essay.zaloapp.domain.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class CommentUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //@MapsId("post_id")
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    //@MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isUserLike;

    private Date createdAt;

    public CommentUser(Comment comment, User user, Boolean isUserLike) {
        this.comment = comment;
        this.user = user;
        this.isUserLike = isUserLike;
        this.createdAt = new Date( new Date().getTime() + 7 * 60 * 60*1000);
    }
}
