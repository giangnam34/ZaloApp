package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.MessageChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageChatRepository extends JpaRepository<MessageChat, Long> {

    @Query("SELECT m FROM MessageChat m WHERE m.groupChat.id = :groupId AND m.deleted = false AND m.replyMessage IS NULL ORDER BY m.sendAt DESC")
    Page<MessageChat> findAllByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT m FROM MessageChat m WHERE m.groupChat.id = :groupId AND m.deleted = false AND m.replyMessage IS NOT NULL ORDER BY m.sendAt DESC")
    Page<MessageChat> findAllByGroupIdAndReplyMessage(@Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM MessageChat m WHERE m.seen = false AND m.groupChat.id = :groupId AND m.user.phoneNumber = :phoneNumberUser")
    int countBySeenFalseAndGroupId(@Param("groupId") Long groupId, @Param("phoneNumberUser") String phoneNumberUser);

    @Query("SELECT m FROM MessageChat m WHERE m.groupChat.id = :groupId ORDER BY m.sendAt DESC")
    List<MessageChat> findLatestMessageByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT m FROM MessageChat m WHERE m.groupChat.id = :groupId AND m.user.phoneNumber = :phoneNumberUser")
    List<MessageChat> findAllByGroupIdAndUserPhoneNumber(@Param("groupId") Long groupId, @Param("phoneNumberUser") String phoneNumberUser);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MessageChat m WHERE m.id = :messageId AND m.deleted = false")
    boolean existsByIdAndNotDeleted(@Param("messageId") Long messageId);

    @Query("SELECT m FROM MessageChat m WHERE m.id = :messageId AND m.deleted = false")
    Optional<MessageChat> findByIdAndNotDeleted(@Param("messageId") Long messageId);
}