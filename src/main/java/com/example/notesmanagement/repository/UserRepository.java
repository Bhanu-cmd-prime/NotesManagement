package com.example.notesmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notesmanagement.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByUserName(String username);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

}
