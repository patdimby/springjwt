package com.dimbisoapatrick.springjwt.repository;

import java.util.Optional;

import com.dimbisoapatrick.springjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    
}
