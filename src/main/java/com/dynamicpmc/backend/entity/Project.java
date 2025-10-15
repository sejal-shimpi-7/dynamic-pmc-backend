package com.dynamicpmc.backend.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String location;

    // RELATIONSHIP CHANGED: Many Projects belong to One Category
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // One Project can have multiple images
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("project-images")
    private List<ProjectImage> images;

    // Constructors, getters, and setters...
    public Project() {}

    // CONSTRUCTOR UPDATED
    public Project(String title, String description, String location, Category category) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
    }

    // Getters and setters for all fields...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // GETTER AND SETTER UPDATED
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<ProjectImage> getImages() { return images; }
    public void setImages(List<ProjectImage> images) { this.images = images; }
}