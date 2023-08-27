package com.dev.vault.projectservice.util;

import com.dev.vault.projectservice.model.entity.Project;
import com.dev.vault.projectservice.repository.ProjectRepository;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
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
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the project with {{" + projectId + "}} wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }

}
