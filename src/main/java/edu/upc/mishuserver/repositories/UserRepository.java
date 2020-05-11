package edu.upc.mishuserver.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import edu.upc.mishuserver.dto.User;

public interface  UserRepository extends JpaRepository<User, Long> ,JpaSpecificationExecutor<User>{
    
    public User  findByUsername(String username);
    
}