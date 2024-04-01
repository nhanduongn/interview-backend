package com.nhandn.backend.repository;

import java.util.Optional;

import com.nhandn.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);


}
