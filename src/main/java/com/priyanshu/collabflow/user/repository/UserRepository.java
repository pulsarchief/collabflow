package com.priyanshu.collabflow.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.priyanshu.collabflow.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}