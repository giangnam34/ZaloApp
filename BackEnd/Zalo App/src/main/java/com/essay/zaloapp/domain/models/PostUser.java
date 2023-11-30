package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.PostUserType;
import com.essay.zaloapp.domain.models.Composite.PostUserId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class PostUser {

    @EmbeddedId
    private PostUserId postUserId;

    @Enumerated(EnumType.STRING)
    private PostUserType postUserType;
}
