package com.dynamicpmc.backend;

import com.dynamicpmc.backend.entity.Category;
import com.dynamicpmc.backend.entity.Project;
import com.dynamicpmc.backend.repository.CategoryRepository;
import com.dynamicpmc.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void run(String... args) throws Exception {
        // THIS IS THE CORRECTED LOGIC
        // Only add data if the CATEGORY table is empty
        if (categoryRepository.count() == 0) {
            System.out.println("Database is empty. Seeding initial data...");

            // --- Create Categories ---
            Category consultancy = new Category("Consultancy Projects", "Expert consultancy services for architectural projects.");
            Category architectural = new Category("Architectural Projects", "Innovative and modern architectural designs.");
            Category interior = new Category("Interior Projects", "Creative and functional interior designs.");
            
            // Save categories to the database so they get an ID
            categoryRepository.save(consultancy);
            categoryRepository.save(architectural);
            categoryRepository.save(interior);

            // --- Create Projects ---
            Project project1 = new Project("Modern Villa Design", "A stunning modern villa with a minimalist aesthetic.", "Mumbai, India", architectural);
            Project project2 = new Project("Corporate Tower Blueprint", "Structural consultation for a new high-rise.", "Pune, India", consultancy);
            Project project3 = new Project("Luxury Apartment Staging", "Interior design for a high-end residential apartment.", "Delhi, India", interior);
            
            // Save projects to the database
            projectRepository.save(project1);
            projectRepository.save(project2);
            projectRepository.save(project3);

            System.out.println("Initial data has been seeded successfully.");
        } else {
            System.out.println("Database already contains data. Skipping seeding.");
        }
    }
}