package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Composite.FriendsId;
import com.essay.zaloapp.domain.models.Friends;
import com.essay.zaloapp.domain.models.User;
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

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friends f " +
            "WHERE ((f.user1 = :user1 AND f.user2 = :user2 AND f.isBlock = :userId) OR " +
            "(f.user1 = :user2 AND f.user2 = :user1 AND f.isBlock = :userId))")
    boolean existsByUser1AndUser2AndIsBlock(@Param("user1") User user1,
                                            @Param("user2") User user2,
                                            @Param("userId") Long userId);
};