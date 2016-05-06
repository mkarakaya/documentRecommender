package com.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.WSUser;

import java.util.Optional;

@Repository // hlltarakci added
public interface UserRepository extends JpaRepository<WSUser, Long> {
    Optional<WSUser> findOneByUsername(String username);
}
