package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.Shops;
import com.example.be_kiotviet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.role r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    long countByIsActiveTrue();

    List<User> findByIsActiveTrue();
    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.role r " +
            "JOIN FETCH u.shop s " +
            "WHERE u.shop.id = :shopId AND r.id = :roleId")
    Optional<User> findByShopIdAndRoleId(@Param("shopId") Long shopId, @Param("roleId") Long roleId);
}
