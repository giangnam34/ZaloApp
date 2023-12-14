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

//    @EmbeddedId
//    private PostUserId postUserId = new PostUserId();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //@MapsId("post_id")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    //@MapsId("user_id")
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
