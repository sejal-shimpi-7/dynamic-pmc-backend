package com.dynamicpmc.backend.service;

import com.dynamicpmc.backend.entity.Project;
import com.dynamicpmc.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    
    @Transactional(readOnly=true)
    public List<Project> getProjectsByCategory(Long categoryId) {
        return projectRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly=true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }


    public boolean existsById(Long id) {
        return projectRepository.existsById(id);
    }

    public long countProjects() {
        return projectRepository.count();
    }
}