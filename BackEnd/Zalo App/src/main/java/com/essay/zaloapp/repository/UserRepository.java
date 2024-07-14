package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.enums.Status;
import com.essay.zaloapp.domain.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByPhoneNumber(String phoneNumber);

    User findById(Long id);

    Boolean existsUserByPhoneNumber(String phoneNumber);

    Boolean existsUserById(Long userId);

    List<User> findAllByStatus(Status status);

    List<User> findAll();
}