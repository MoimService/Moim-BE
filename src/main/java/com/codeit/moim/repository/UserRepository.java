package com.codeit.moim.repository;

import com.codeit.moim.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByName(@Param("name") String name);

    boolean existsByEmail(@Param("email") String email);
}
