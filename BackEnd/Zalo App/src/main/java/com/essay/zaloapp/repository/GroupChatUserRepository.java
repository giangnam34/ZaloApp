package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Composite.GroupChatUserId;
import com.essay.zaloapp.domain.models.GroupChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatUserRepository extends JpaRepository<GroupChatUser, GroupChatUserId> {
}