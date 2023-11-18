package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "poll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Poll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String pollName;

	private Date createdAt;

	private Date EndAt;

	private Boolean isMultipleSelect;

	private Boolean isApproveAddOption;

	private Boolean isHideResultWhenNotVoted;

	private Boolean isHideVoters;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "group_chat_id")
	private GroupChat groupChat;

	@OneToMany(mappedBy = "poll")
	private List<OptionPoll> optionPollList;

}
