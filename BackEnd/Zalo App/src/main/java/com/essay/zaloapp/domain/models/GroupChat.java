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
	private int id;

	private String groupName;

	private Boolean isChangeNameAndAvatar;

	private Boolean isPinMessage;

	private Boolean isCreateNewNote;

	private Boolean isCreateNewPoll;

	private Boolean isSendMessage;

	@OneToMany(mappedBy = "groupChat")
	private List<MessageChat> messageChatList;

	@OneToMany(mappedBy = "groupChat")
	private List<Poll> pollList;

}
