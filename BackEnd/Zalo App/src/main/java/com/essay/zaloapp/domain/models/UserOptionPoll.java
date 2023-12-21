package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_option_poll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOptionPoll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userOptionPollId;

	private Date votedAt;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "option_id")
	private OptionPoll optionPoll;
}
