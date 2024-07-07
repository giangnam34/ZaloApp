package com.essay.zaloapp.domain.payload.response.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralInfoResponse {
    private Long membersCount = null;
    private Long onlineMembersCount = null;
    private Long groupsCount = null;
    private Long postsCount = null;
}
