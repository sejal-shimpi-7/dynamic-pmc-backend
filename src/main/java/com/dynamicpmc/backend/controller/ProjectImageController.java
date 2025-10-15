package com.dynamicpmc.backend.controller;

import com.dynamicpmc.backend.entity.ProjectImage;
import com.dynamicpmc.backend.service.FileStorageService;
import com.dynamicpmc.backend.service.ProjectImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/project-images")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
//@CrossOrigin
public class ProjectImageController {

    @Autowired
    private ProjectImageService projectImageService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<ProjectImage> getAllImages() {
        return projectImageService.getAllImages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectImage> getImageById(@PathVariable Long id) {
        return projectImageService.getImageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProjectImage createImage(@RequestBody ProjectImage projectImage) {
        return projectImageService.saveImage(projectImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectImage> updateImage(@PathVariable Long id, @RequestBody ProjectImage projectImage) {
        return projectImageService.getImageById(id)
                .map(existing -> {
                    existing.setImageUrl(projectImage.getImageUrl());
                    existing.setDescription(projectImage.getDescription());
                    return ResponseEntity.ok(projectImageService.saveImage(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        if (projectImageService.getImageById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        projectImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload/{projectId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {
        
        try {
            ProjectImage image = projectImageService.uploadImage(projectId, file, description);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectImage> getImagesByProject(@PathVariable Long projectId) {
        return projectImageService.getImagesByProject(projectId);
    }

    // NEW ENDPOINT: Serve actual image files
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.loadFile(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filename);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String filename) {
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFilename.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }
}