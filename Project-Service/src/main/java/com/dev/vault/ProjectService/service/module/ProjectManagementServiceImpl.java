package com.dev.vault.ProjectService.service.module;

import com.dev.vault.ProjectService.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.ProjectService.model.dto.Roles;
import com.dev.vault.ProjectService.model.dto.User;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.model.entity.ProjectMembers;
import com.dev.vault.ProjectService.model.entity.UserProjectRole;
import com.dev.vault.ProjectService.model.request.ProjectRequest;
import com.dev.vault.ProjectService.repository.ProjectMembersRepository;
import com.dev.vault.ProjectService.repository.ProjectRepository;
import com.dev.vault.ProjectService.repository.UserProjectRoleRepository;
import com.dev.vault.ProjectService.service.RetrieveDataService;
import com.dev.vault.ProjectService.service.interfaces.ProjectManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dev.vault.ProjectService.model.dto.Role.PROJECT_LEADER;

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
    private final RetrieveDataService retrieveDataService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ProjectRequest createProject(ProjectRequest projectRequest) {
        // Check if a project with the same name already exists
        validateProjectNameUnique(projectRequest.getProjectName());

        // Get the `PROJECT_LEADER` role
        Roles projectLeaderRole = retrieveDataService.get_ProjectLeaderRole_FromAuthenticationService(PROJECT_LEADER);

        // Get the current user and Add the `PROJECT_LEADER` role to the current user and save to db
        User currentUser = retrieveDataService.get_CurrentUser_FromAuthenticationService();
        currentUser.getRoles().add(projectLeaderRole);
        retrieveDataService.save_UserInfo_ForAuthenticationService(currentUser);

        // Map the projectRequest to a Project object and set the leader to the current user
        Project project = createProjectFromRequest(projectRequest, currentUser.getUserId());

        // Save the project to the database
        // Create a new `ProjectMembers` object for mapping a member for the current user
        // Create a new `UserProjectRole` object for mapping `PROJECT_LEADER` role for the user and project
        saveProjectAndRelatedInfo(project, projectLeaderRole.getRoleId(), currentUser.getUserId());

        // Return a `ProjectRequest` object with the project information as response
        return createProjectRequestFromProject(project);
    }

    private void validateProjectNameUnique(String projectName) {
        Optional<Project> foundProject = projectRepository.findByProjectNameAllIgnoreCase(projectName);
        if (foundProject.isPresent()) {
            log.info("⚠️this project already exists! provide a unique name");
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
