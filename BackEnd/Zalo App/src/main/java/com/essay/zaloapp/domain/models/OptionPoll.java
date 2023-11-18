package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "option_poll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionPoll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String optionName;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "poll_id")
	private Poll poll;

	@OneToOne(mappedBy = "optionPoll")
	private UserOptionPoll userOptionPoll;
}
