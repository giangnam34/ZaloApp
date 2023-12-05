package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.models.Composite.PostUserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class PostUser {

    @EmbeddedId
    private PostUserId postUserId = new PostUserId();

    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PostUserType postUserType;

    public PostUser(Post post, User user, PostUserType postUserType) {
        this.post = post;
        this.user = user;
        this.postUserType = postUserType;
    }
}
