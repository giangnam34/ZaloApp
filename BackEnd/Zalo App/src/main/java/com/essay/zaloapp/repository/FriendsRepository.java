package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Composite.FriendsId;
import com.essay.zaloapp.domain.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendsRepository extends JpaRepository<Friends, FriendsId> {

    List<Friends> findByFriendsIdUser1(Long user1);

    List<Friends> findByFriendsIdUser2(Long user2);

    Boolean existsFriendsByFriendsId(FriendsId friendsId);

    Friends findByFriendsId(FriendsId friendsId);

    @Query("SELECT fr FROM Friends fr WHERE (fr.user1.id = :userId OR fr.user2.id = :userId ) AND fr.friendStatus = 'IsFriend' ")
    List<Friends> findAllByUserId(@Param("userId") Long userId);
};