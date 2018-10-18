package com.sven.ce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sven.ce.model.User;

/**
 * {@code User} repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

    Optional<User> findByUsername(String userName);
}