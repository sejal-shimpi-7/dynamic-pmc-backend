package com.dynamicpmc.backend.repository;

import com.dynamicpmc.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    // THIS IS THE NEW, OVERRIDDEN METHOD TO FIX THE CRASH
    @Override
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.images")
    List<Project> findAll();

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.images WHERE p.category.id = :categoryId")
    List<Project> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Project> findByIdWithImages(@Param("id") Long id);
}