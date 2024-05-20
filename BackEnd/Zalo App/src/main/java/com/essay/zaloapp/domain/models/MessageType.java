package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "message_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String messageTypeName;

//	@OneToMany(mappedBy = "messageType")
//	private List<MessageChat> messageChatList;
}
