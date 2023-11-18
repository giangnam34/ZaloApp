package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.MessageChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageChatRepository extends JpaRepository<MessageChat, Integer> {
}