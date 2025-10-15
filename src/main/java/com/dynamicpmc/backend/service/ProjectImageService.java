package com.dynamicpmc.backend.service;

import com.dynamicpmc.backend.entity.Project;
import com.dynamicpmc.backend.entity.ProjectImage;
import com.dynamicpmc.backend.repository.ProjectImageRepository;
import com.dynamicpmc.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectImageService {

    @Autowired
    private ProjectImageRepository projectImageRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // INJECT THE NEW CLOUDINARY SERVICE
    @Autowired
    private CloudinaryService cloudinaryService;
    
    // (We no longer need the old FileStorageService)

    public List<ProjectImage> getAllImages() {
        return projectImageRepository.findAll();
    }

    public Optional<ProjectImage> getImageById(Long id) {
        return projectImageRepository.findById(id);
    }

    public ProjectImage saveImage(ProjectImage projectImage) {
        return projectImageRepository.save(projectImage);
    }

    public void deleteImage(Long id) {
        projectImageRepository.deleteById(id);
    }

    public List<ProjectImage> getImagesByProject(Long projectId) {
        return projectImageRepository.findByProjectId(projectId);
    }

    // THIS METHOD IS NOW UPDATED TO USE CLOUDINARY
    public ProjectImage uploadImage(Long projectId, MultipartFile file, String description) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        // Upload file to Cloudinary and get the public URL
        String imageUrl = cloudinaryService.uploadFile(file);

        // Create ProjectImage entity with the Cloudinary URL
        ProjectImage projectImage = new ProjectImage();
        projectImage.setImageUrl(imageUrl); // <-- The URL now comes from Cloudinary
        projectImage.setDescription(description);
        projectImage.setProject(project);

        return projectImageRepository.save(projectImage);
    }
}