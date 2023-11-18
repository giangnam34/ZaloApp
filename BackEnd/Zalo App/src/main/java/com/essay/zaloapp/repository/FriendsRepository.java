package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Composite.FriendsId;
import com.essay.zaloapp.domain.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friends, FriendsId> {
}