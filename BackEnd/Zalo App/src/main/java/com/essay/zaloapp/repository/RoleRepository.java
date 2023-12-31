package com.essay.zaloapp.repository;

import com.essay.zaloapp.domain.enums.RoleName;
import com.essay.zaloapp.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName roleName);
}
