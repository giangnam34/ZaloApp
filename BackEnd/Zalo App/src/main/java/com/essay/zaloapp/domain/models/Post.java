package com.essay.zaloapp.domain.models;

import com.nimbusds.oauth2.sdk.id.Audience;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    private Date updateAt;

    // Nội dung comment
    private String contentPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "post_resource", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<Resource> resourceList;
}
