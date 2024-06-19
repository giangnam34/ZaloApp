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

	@Column(length = 15000)
	private String content;

	@Column(name = "isSystem", nullable = false, columnDefinition = "boolean default false")
	private Boolean isSystem = false;

	@Column(name = "saved", nullable = false, columnDefinition = "boolean default true")
	private Boolean saved = true;

	@Column(name = "distributed", nullable = false, columnDefinition = "boolean default false")
	private Boolean distributed = false;

	@Column(name = "seen", nullable = false, columnDefinition = "boolean default false")
	private Boolean seen = false;

	@Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean deleted = false;

	@Column(name = "failure", nullable = false, columnDefinition = "boolean default false")
	private Boolean failure = false;

	@Column(name = "disableActions", nullable = false, columnDefinition = "boolean default false")
	private Boolean disableActions = false;

	@Column(name = "disableReactions", nullable = false, columnDefinition = "boolean default false")
	private Boolean disableReactions = false;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private GroupChat groupChat;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "reply_message_id")
	private MessageChat replyMessage;

	@OneToMany(mappedBy="replyMessage")
	private List<MessageChat> replyMessageList;

	@OneToMany(mappedBy = "messageChat", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reaction> reactions;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "message_resource", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
	private List<Resource> files;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "message_tagUser", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> tagUsers;
}
