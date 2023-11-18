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
public class FriendsId implements Serializable {

	public String phoneNumberUser1;
	public String phoneNumberUser2;
}
