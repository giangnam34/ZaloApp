package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
}