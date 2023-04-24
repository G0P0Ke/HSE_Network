package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.security.Role;
import com.andreev.coursework.core.model.security.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName name);
}