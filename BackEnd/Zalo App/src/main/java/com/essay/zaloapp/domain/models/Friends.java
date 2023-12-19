package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.FriendStatus;
import com.essay.zaloapp.domain.models.Composite.FriendsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@ManyToOne(cascade = CascadeType.ALL)
	@MapsId("user1")
	@JoinColumn(name = "user_id_1")
	private User user1;

	@ManyToOne(cascade = CascadeType.ALL)
	@MapsId("user2")
	@JoinColumn(name = "user_id_2")
	private User user2;

	private Date friendFrom;

	private Long isBlock;

	private Long isDelete;

	private String nickNameFromUser1;

	private String nickNameFromUser2;

	// false nếu là user 1, true nếu là user 2
	private Boolean sendInviteBy;

	@Enumerated(EnumType.STRING)
	private FriendStatus friendStatus;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_from_user1")
	private CategoryUser categoryFromUser1;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_from_user2")
	private CategoryUser categoryFromUser2;

	public Friends(FriendsId friendsId, Date friendFrom, Long isBlock, Long isDelete, String nickNameFromUser1, String nickNameFromUser2, Boolean sendInviteBy, FriendStatus friendStatus) {
		this.friendsId = friendsId;
		this.friendFrom = friendFrom;
		this.isBlock = isBlock;
		this.isDelete = isDelete;
		this.nickNameFromUser1 = nickNameFromUser1;
		this.nickNameFromUser2 = nickNameFromUser2;
		this.sendInviteBy = sendInviteBy;
		this.friendStatus = friendStatus;
	}
}
