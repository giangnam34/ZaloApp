package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.models.Composite.GroupChatUserId;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "group_chat_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GroupChatUser {

	@EmbeddedId
	private GroupChatUserId id;

	private Boolean isUserCreateGroup;

	private Boolean isOwner;

	private Boolean isAdmin;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private CategoryUser category;
}
