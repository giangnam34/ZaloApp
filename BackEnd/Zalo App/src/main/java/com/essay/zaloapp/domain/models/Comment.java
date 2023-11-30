package com.essay.zaloapp.domain.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "comment_top_id")
    private Comment comment;

    private Date createdAt;

    private Date updatedAt;

    // Nội dung comment
    private String contentComment;

    @ManyToMany
    @JoinTable(name = "comment_resource", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<Resource> resourceList;
}
