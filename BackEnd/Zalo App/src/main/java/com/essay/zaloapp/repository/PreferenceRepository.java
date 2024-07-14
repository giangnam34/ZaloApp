package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.models.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findAll();

    List<Preference> findByAgeUserAfterAndAgeUserBefore(Long minAge, Long maxAge);

    Preference findFirstByUserIDAndPostID(Long userID, Long postID);
}
