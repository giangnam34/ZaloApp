package com.essay.zaloapp.domain.models.Composite;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendsId implements Serializable {

	@Column(name = "user_id1")
	private Long user1;

	@Column(name = "user_id2")
	private Long user2;

}
