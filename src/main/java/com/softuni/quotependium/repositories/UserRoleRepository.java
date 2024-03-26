package com.softuni.quotependium.repositories;

import com.softuni.quotependium.domain.entities.UserRoleEntity;
import com.softuni.quotependium.domain.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByRole(UserRoleEnum userRoleEnum);
}
