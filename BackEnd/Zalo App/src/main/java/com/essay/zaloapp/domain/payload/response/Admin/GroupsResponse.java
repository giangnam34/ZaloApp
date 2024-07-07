package com.essay.zaloapp.domain.payload.response.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupsResponse {
    private Long id = null;
    private String groupName = null;
    private Long membersCount = null;
    private Long messagesCount = null;
}
