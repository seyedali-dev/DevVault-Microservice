package com.dev.vault.ProjectService.util;

import com.dev.vault.ProjectService.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.ProjectService.exceptions.ResourceNotFoundException;
import com.dev.vault.ProjectService.model.dto.User;
import com.dev.vault.ProjectService.model.entity.JoinProject;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.model.entity.ProjectMembers;
import com.dev.vault.ProjectService.repository.JoinProjectRepository;
import com.dev.vault.ProjectService.repository.ProjectMembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Implementation of ProjectUtils specifically for handling join project requests.
 * <p>
 * Different implementation of {@link ProjectUtils#isMemberOfProject(Project, User) isMemberOfProject(Project, User)} for JoinRequestService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JoinRequestProjectUtilsImpl implements ProjectUtils {

    private final ProjectMembersRepository projectMembersRepository;
    private final JoinProjectRepository joinProjectRequestRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaderOrAdminOfProject(Project project, User user) {
        return false;
    }


    /**
     * Checks if the user is already a member of the specified project or has already sent a join project request for the project.
     *
     * @param project the project to check for membership
     * @param user    the user to check for membership
     * @return true if the user is already a member of the project or has already sent a join project request for the project, false otherwise
     * @throws ResourceNotFoundException      if the project cannot be found
     * @throws ResourceAlreadyExistsException if the user has already sent a join project request for the project
     */
    @Override
    public boolean isMemberOfProject(Project project, User user) {
        // Check if the user has already sent a join project request for the project
        Optional<JoinProject> joinRequest
                = joinProjectRequestRepository.findByProject_ProjectIdAndUserId(project.getProjectId(), user.getUserId());

        if (joinRequest.isPresent())
            throw new ResourceAlreadyExistsException("This user is already a part of this project", BAD_REQUEST, BAD_REQUEST.value());

        Optional<ProjectMembers> member = projectMembersRepository.findByProject_ProjectNameAndUserId(project.getProjectName(), user.getUserId());
        // Check if the user is already a member of the project
        return member.isPresent();
    }

}
