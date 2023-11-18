package com.essay.zaloapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "category_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUser{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private int indexCategory;

	private Date createdAt;

	private Date updatedAt;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "phone_number_user")
	private User user;

	@OneToMany(mappedBy="categoryFromUser1")
	private List<Friends> friends1;

	@OneToMany(mappedBy="categoryFromUser2")
	private List<Friends> friends2;

	@OneToMany(mappedBy="category")
	private List<GroupChatUser> groupChat;

}
