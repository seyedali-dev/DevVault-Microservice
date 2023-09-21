package com.dev.vault.ProjectService.repository;

import com.dev.vault.ProjectService.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectName(String projectName);


    List<Project> findByProjectNameContaining(String projectName);


    Optional<Project> findByProjectNameAllIgnoreCase(String projectName);

}