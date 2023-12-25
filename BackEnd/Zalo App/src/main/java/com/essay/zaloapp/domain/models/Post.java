package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.Audience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    private Date updatedAt;

    // Nội dung comment
    private String contentPost;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience")
    private Audience audienceValue;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostUser> postUserList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_resource", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<Resource> resourceList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_top_id")
    private Post postTop;

    @OneToMany(mappedBy="postTop")
    private List<Post> postTopList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList;


    public Post(Date createdAt, Date updatedAt, String contentPost, Audience value, User user, List<Resource> resourceList) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.contentPost = contentPost;
        this.audienceValue = value;
        this.user = user;
        this.resourceList = resourceList;
    }

//    @PreUpdate
//    public void updateTime(){
//        this.updatedAt = new Date(new Date().getTime() + 7*60*60*1000);
//    }
}