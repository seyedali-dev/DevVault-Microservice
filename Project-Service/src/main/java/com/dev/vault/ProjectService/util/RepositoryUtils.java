package com.dev.vault.ProjectService.util;

import com.dev.vault.ProjectService.exceptions.ResourceNotFoundException;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryUtils {

    private final ProjectRepository projectRepository;

    public Project findProjectById_OrElseThrow_ResourceNotFoundException(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.info("😖 huh... it seems the project with {{}} wasn't found in the db 😖", projectId);
                    return new ResourceNotFoundException("", NOT_FOUND, NOT_FOUND.value());
                });
    }

}
