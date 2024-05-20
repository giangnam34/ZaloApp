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

	@Column(name = "isUserCreateGroup", nullable = false, columnDefinition = "boolean default true")
	private Boolean isUserCreateGroup = true;

	@Column(name = "isOwner", nullable = false, columnDefinition = "boolean default true")
	private Boolean isOwner = true;

	@Column(name = "isAdmin", nullable = false, columnDefinition = "boolean default false")
	private Boolean isAdmin = false;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted = false;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private CategoryUser category;
}
