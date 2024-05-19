package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "group_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String groupName;

	@Column(name = "isChangeNameAndAvatar", nullable = false, columnDefinition = "boolean default false")
	private Boolean isChangeNameAndAvatar = false;

	@Column(name = "isPinMessage", nullable = false, columnDefinition = "boolean default false")
	private Boolean isPinMessage = false;

	@Column(name = "isCreateNewNote", nullable = false, columnDefinition = "boolean default false")
	private Boolean isCreateNewNote = false;

	@Column(name = "isCreateNewPoll", nullable = false, columnDefinition = "boolean default false")
	private Boolean isCreateNewPoll = false;

	@Column(name = "isSendMessage", nullable = false, columnDefinition = "boolean default false")
	private Boolean isSendMessage = false;

	private String avatar;

	@Column(name = "deletedCount", nullable = false, columnDefinition = "int default 0")
	private Integer deletedCount = 0;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted = false;

	@OneToMany(mappedBy = "groupChat")
	private List<MessageChat> messageChatList;

	@OneToMany(mappedBy = "groupChat")
	private List<Poll> pollList;

}
