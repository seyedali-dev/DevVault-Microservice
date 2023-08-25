package com.dev.vault.projectservice.util;

import com.dev.vault.projectservice.model.dto.UserMembersDto;
import com.dev.vault.projectservice.model.entity.JoinProject;
import com.dev.vault.projectservice.model.entity.Project;
import com.dev.vault.projectservice.model.entity.ProjectMembers;
import com.dev.vault.projectservice.repository.JoinProjectRepository;
import com.dev.vault.projectservice.repository.ProjectMembersRepository;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Implementation of ProjectUtils specifically for handling join project requests.
 * <p>
 * Different implementation of {@link ProjectUtils#isMemberOfProject(Project, UserDTO) isMemberOfProject(Project, User)} for JoinRequestService.
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
    public boolean isLeaderOrAdminOfProject(Project project, UserDTO userDTO) {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMembersDto> getUserDtoList(Project project) {
        return null;
    }


    /**
     * Checks if the user is already a member of the specified project or has already sent a join project request for the project.
     *
     * @param project the project to check for membership
     * @param userDTO the user to check for membership
     * @return true if the user is already a member of the project or has already sent a join project request for the project, false otherwise
     * @throws ResourceNotFoundException      if the project cannot be found
     * @throws ResourceAlreadyExistsException if the user has already sent a join project request for the project
     */
    @Override
    public boolean isMemberOfProject(Project project, UserDTO userDTO) {
        // Check if the user has already sent a join project request for the project
        Optional<JoinProject> joinRequest
                = joinProjectRequestRepository.findByProject_ProjectIdAndUserId(project.getProjectId(), userDTO.getUserId());

        if (joinRequest.isPresent())
            throw new ResourceAlreadyExistsException("😊 You already sent a join request for this project 😊", BAD_REQUEST, BAD_REQUEST.value());

        Optional<ProjectMembers> member = projectMembersRepository.findByProject_ProjectNameAndUserId(project.getProjectName(), userDTO.getUserId());
        // Check if the user is already a member of the project
        return member.isPresent();
    }

}
