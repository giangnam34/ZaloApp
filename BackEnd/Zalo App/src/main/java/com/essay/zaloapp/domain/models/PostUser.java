package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.PostUserType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PostUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PostUserType postUserType;

    private Date createdAt;

    public PostUser(Post post, User user, PostUserType postUserType) {
        this.post = post;
        this.user = user;
        this.postUserType = postUserType;
        this.createdAt = new Date( new Date().getTime() + 7 * 60 * 60*1000);
    }
}
