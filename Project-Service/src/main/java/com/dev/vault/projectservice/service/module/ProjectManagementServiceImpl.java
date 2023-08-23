package com.dev.vault.projectservice.service.module;

import com.dev.vault.projectservice.feign.client.AuthUserFeignClient;
import com.dev.vault.projectservice.model.entity.Project;
import com.dev.vault.projectservice.model.entity.ProjectMembers;
import com.dev.vault.projectservice.model.entity.UserProjectRole;
import com.dev.vault.projectservice.model.request.ProjectRequest;
import com.dev.vault.projectservice.repository.ProjectMembersRepository;
import com.dev.vault.projectservice.repository.ProjectRepository;
import com.dev.vault.projectservice.repository.UserProjectRoleRepository;
import com.dev.vault.projectservice.service.interfaces.ProjectManagementService;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Service Implementation of Project Creation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectManagementServiceImpl implements ProjectManagementService {

    private final UserProjectRoleRepository userProjectRoleRepository;
    private final ProjectMembersRepository projectMembersRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final AuthUserFeignClient authUserFeignClient;
    private final HttpServletRequest httpServletRequest;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ProjectRequest createProject(ProjectRequest projectRequest) {
        // Check if a project with the same name already exists
        validateProjectNameUnique(projectRequest.getProjectName());

        // Get the current user and Add the `PROJECT_LEADER` role to the current user and save to db
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        Long currentUserId = authUserFeignClient.getCurrentUserId(requestHeader);

        // Make API call to `AUTHENTICATION-SERVICE` to add `PROJECT_LEADER` role to the `current user`
        authUserFeignClient.addProjectLeaderRoleToUser(currentUserId);

        // Map the projectRequest to a Project object and set the leader to the current user
        Project project = createProjectFromRequest(projectRequest, currentUserId);

        // Save the project to the database
        // Create a new `ProjectMembers` object for mapping a member for the current user
        // Create a new `UserProjectRole` object for mapping `PROJECT_LEADER` role for the user and project
        Long projectLeaderRoleId = authUserFeignClient.getProjectLeaderRoleId();
        saveProjectAndRelatedInfo(project, currentUserId, projectLeaderRoleId);

        // Return a `ProjectRequest` object with the project information as response
        return createProjectRequestFromProject(project);
    }

    private void validateProjectNameUnique(String projectName) {
        Optional<Project> foundProject = projectRepository.findByProjectNameAllIgnoreCase(projectName);
        if (foundProject.isPresent()) {
            log.error("⚠️this project already exists! provide a unique name");
            throw new ResourceAlreadyExistsException(String.format("Project with provided name: {%s} already exists", projectName));
        }
    }


    private Project createProjectFromRequest(ProjectRequest projectRequest, Long leaderId) {
        Project project = modelMapper.map(projectRequest, Project.class);

        project.setLeaderId(leaderId);
        project.incrementMemberCount();

        return project;
    }


    private void saveProjectAndRelatedInfo(Project project, Long currentUserId, Long projectLeaderRole) {
        projectRepository.save(project);

        ProjectMembers projectMembers = new ProjectMembers(currentUserId, project);
        projectMembersRepository.save(projectMembers);

        UserProjectRole userProjectRole = new UserProjectRole(currentUserId, projectLeaderRole, project);
        userProjectRoleRepository.save(userProjectRole);
    }


    private ProjectRequest createProjectRequestFromProject(Project project) {
        return ProjectRequest.builder()
                .projectName(project.getProjectName())
                .projectDescription(project.getDescription())
                .createdAt(project.getCreatedAt())
                .creationTime(project.getCreationTime())
                .build();
    }

}
