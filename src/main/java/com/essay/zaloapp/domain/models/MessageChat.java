package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageChat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Date sendAt;

	private Date updatedAt;

	private Boolean isImportant;

	private Boolean isUrgent;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private GroupChat groupChat;

	@ManyToOne
	@JoinColumn(name = "phone_number_user")
	private User user;

	@ManyToOne
	@JoinColumn(name = "message_type")
	private MessageType messageType;
}
