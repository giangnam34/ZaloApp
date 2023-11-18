package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.models.Composite.FriendsId;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "friends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friends {

	@EmbeddedId
	private FriendsId friendsId;

	private Date friendFrom;

	private Boolean isBlock;

	private Boolean isDelete;

	private String nickNameFromUser1;

	private String nickNameFromUser2;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_from_user1")
	private CategoryUser categoryFromUser1;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_from_user2")
	private CategoryUser categoryFromUser2;


}
