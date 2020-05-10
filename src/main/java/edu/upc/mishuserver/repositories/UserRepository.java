package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.dto.User;

public interface UserRepository extends JpaRepository<User,Long>{
    
}