package com.essay.zaloapp.domain.models.Composite;

import com.essay.zaloapp.domain.models.Post;
import com.essay.zaloapp.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostUserId implements Serializable {

    @Column(name = "post_id")
    private Long post_id;

    @Column(name = "user_id")
    private Long user_id;

}
