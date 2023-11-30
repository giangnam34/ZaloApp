package com.essay.zaloapp.domain.models.Composite;

import com.essay.zaloapp.domain.models.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendsId implements Serializable {

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "phoneNumberUser1")
	private User user1;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "phoneNumberUser2")
	private User user2;
}
