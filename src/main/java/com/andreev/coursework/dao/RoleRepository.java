package com.andreev.coursework.dao;

import com.andreev.coursework.entity.security.Role;
import com.andreev.coursework.entity.security.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName name);
}