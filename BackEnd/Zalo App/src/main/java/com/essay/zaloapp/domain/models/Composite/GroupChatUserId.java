package com.essay.zaloapp.domain.models.Composite;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GroupChatUserId implements Serializable {

	private Long groupId;
	private String phoneNumberUser;

}
