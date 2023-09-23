package com.dev.vault.ProjectService.util;

import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.model.entity.ProjectMembers;
import com.dev.vault.ProjectService.repository.ProjectMembersRepository;
import com.dev.vault.ProjectService.repository.ProjectRepository;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryUtils {

    private final ProjectRepository projectRepository;
    private final ProjectMembersRepository projectMembersRepository;

    public Project findProjectById_OrElseThrow_ResourceNotFoundException(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems the project with {{}} wasn't found in the db 😖", projectId);
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the project with {{" + projectId + "}} wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }


    public Collection<ProjectMembers> find_ProjectMembersByProjectId(long projectId) {
        return projectMembersRepository.findByProject_ProjectId(projectId);
    }

}
