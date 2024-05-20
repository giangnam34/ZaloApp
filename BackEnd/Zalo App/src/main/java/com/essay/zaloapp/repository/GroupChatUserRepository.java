package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Composite.GroupChatUserId;
import com.essay.zaloapp.domain.models.GroupChat;
import com.essay.zaloapp.domain.models.GroupChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupChatUserRepository extends JpaRepository<GroupChatUser, GroupChatUserId> {
    @Query("SELECT gu1.id.groupId FROM GroupChatUser gu1 " +
            "JOIN GroupChatUser gu2 ON gu1.id.groupId = gu2.id.groupId " +
            "WHERE gu1.id.phoneNumberUser = :senderPhoneNumber " +
            "AND gu2.id.phoneNumberUser = :receiverPhoneNumber")
    List<Long> findGroupChatIdsBySenderAndReceiverPhoneNumbers(@Param("senderPhoneNumber") String senderPhoneNumber, @Param("receiverPhoneNumber") String receiverPhoneNumber);

    @Query("SELECT gcu FROM GroupChatUser gcu WHERE gcu.id.groupId = :groupId AND gcu.isDeleted = false")
    List<GroupChatUser> findAllByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT gcu FROM GroupChatUser gcu WHERE gcu.id.phoneNumberUser = :phoneNumberUser AND gcu.isDeleted = false")
    List<GroupChatUser> findByIdPhoneNumberUser(@Param("phoneNumberUser") String phoneNumberUser);

    @Query("SELECT gcu FROM GroupChatUser gcu WHERE gcu.id.phoneNumberUser = :phoneNumberUser")
    List<GroupChatUser> findAllByPhoneNumberUser(@Param("phoneNumberUser") String phoneNumberUser);
}