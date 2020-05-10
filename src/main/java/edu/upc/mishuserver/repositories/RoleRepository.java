package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.dto.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    
}