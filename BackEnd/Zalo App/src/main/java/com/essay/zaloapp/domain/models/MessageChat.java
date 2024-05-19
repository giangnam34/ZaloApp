package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "message_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageChat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date sendAt;

	private Date updatedAt;

	private String content;

	private Boolean isSystem = false;

	private Boolean saved = true;

	private Boolean distributed = false;

	private Boolean seen = false;

	private Boolean deleted = false;

	private Boolean failure = false;

	private Boolean disableActions = false;

	private Boolean disableReactions = false;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private GroupChat groupChat;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "message_type")
	private MessageType messageType;

//	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "reply_message_id")
//	private ReplyMessage replyMessage;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "reply_message_id")
	private MessageChat replyMessage;

	@OneToMany(mappedBy="replyMessage")
	private List<MessageChat> replyMessageList;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "message_resource", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
	private List<Resource> files;
}
