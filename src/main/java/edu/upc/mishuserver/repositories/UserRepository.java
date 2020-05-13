package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

	User findByEmail(String email);
    
}