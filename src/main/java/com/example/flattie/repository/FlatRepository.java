package com.example.flattie.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.flattie.model.Flat;

/**
 * Repository interface for interacting with Flat entities within
 * the database. Offers several built in methods that are called in
 * FlatService. Future custom queries will be added here and then
 * called with the service class.
 */
@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {
    boolean existsByAddress(String address); // Custom query method to check if an address exists

    Flat findByJoinCode(String joinCode); // Custom query method to find a flat by join code

    @Query("""
                SELECT f FROM Flat f
                LEFT JOIN FETCH f.members m
                LEFT JOIN FETCH m.user
                WHERE f.id = :id
            """)
    Optional<Flat> findByIdWithMembers(@Param("id") Long id);

    @Query("""
              SELECT f
                FROM Flat f
                LEFT JOIN FETCH f.noticeBoard nb
               WHERE f.id = :id
            """)
    Optional<Flat> findByIdWithNotices(@Param("id") Long id);
}