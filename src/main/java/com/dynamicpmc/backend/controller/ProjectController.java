package com.dynamicpmc.backend.controller;

import com.dynamicpmc.backend.entity.Project;
import com.dynamicpmc.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
//@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Project>> getProjectsByCategory(@PathVariable Long categoryId) {
        try {
            List<Project> projects = projectService.getProjectsByCategory(categoryId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            return ResponseEntity.ok(projectService.getAllProjects());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        try {
            return projectService.getProjectById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        try {
            return ResponseEntity.ok(projectService.saveProject(project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        try {
            return projectService.getProjectById(id)
                    .map(existing -> {
                        // Only update allowed fields - SECURITY FIX
                        if (project.getTitle() != null) {
                            existing.setTitle(project.getTitle());
                        }
                        if (project.getDescription() != null) {
                            existing.setDescription(project.getDescription());
                        }
                        if (project.getLocation() != null) {
                            existing.setLocation(project.getLocation());
                        }
                        // Don't allow changing subcategory through this endpoint
                        return ResponseEntity.ok(projectService.saveProject(existing));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            if (projectService.getProjectById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}