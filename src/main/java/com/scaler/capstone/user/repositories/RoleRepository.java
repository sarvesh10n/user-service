package com.scaler.capstone.user.repositories;

import com.scaler.capstone.user.enums.Roles;
import com.scaler.capstone.user.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles name);
}

