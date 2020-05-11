package edu.upc.mishuserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.upc.mishuserver.dto.Password;

public interface PasswordRepository extends JpaRepository<Password, Long>, JpaSpecificationExecutor<Password> {
    
    public List<Password> findByUid(String uid);
}