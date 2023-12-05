package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.Audience;
import com.essay.zaloapp.domain.models.Composite.PostUserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    private Date updateAt;

    // Nội dung comment
    private String contentPost;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience")
    private Audience audienceValue;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostUser> postUserList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_resource", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<Resource> resourceList;

    @ManyToOne
    @JoinColumn(name = "post_top_id")
    private Post postTop;

    @OneToMany(mappedBy="postTop")
    private List<Post> postList;

    public Post(Date createdAt, Date updateAt, String contentPost, Audience value, User user, List<Resource> resourceList) {
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.contentPost = contentPost;
        this.audienceValue = value;
        this.user = user;
        this.resourceList = resourceList;
    }
}
