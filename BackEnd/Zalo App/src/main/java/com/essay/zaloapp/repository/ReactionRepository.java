package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.MessageChat;
import com.essay.zaloapp.domain.models.PostUser;
import com.essay.zaloapp.domain.models.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Reaction r WHERE r.messageChat = :messageChat")
    void deleteByMessageChat(@Param("messageChat") MessageChat messageChat);
}
