package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.dto.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {
    
}