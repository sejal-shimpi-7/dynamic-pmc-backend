package com.dynamicpmc.backend.repository;

import com.dynamicpmc.backend.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    
    @Query("SELECT pi FROM ProjectImage pi WHERE pi.project.id = :projectId")
    List<ProjectImage> findByProjectId(@Param("projectId") Long projectId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM ProjectImage pi WHERE pi.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}