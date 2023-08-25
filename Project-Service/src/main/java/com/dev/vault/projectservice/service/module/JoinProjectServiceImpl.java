package com.dev.vault.projectservice.service.module;

import com.dev.vault.projectservice.feign.client.AuthUserFeignClient;
import com.dev.vault.projectservice.model.entity.JoinProject;
import com.dev.vault.projectservice.model.entity.Project;
import com.dev.vault.projectservice.model.entity.ProjectMembers;
import com.dev.vault.projectservice.model.enums.JoinStatus;
import com.dev.vault.projectservice.model.response.JoinProjectResponse;
import com.dev.vault.projectservice.model.response.JoinResponse;
import com.dev.vault.projectservice.repository.JoinProjectRepository;
import com.dev.vault.projectservice.repository.ProjectMembersRepository;
import com.dev.vault.projectservice.repository.ProjectRepository;
import com.dev.vault.projectservice.service.interfaces.JoinProjectService;
import com.dev.vault.projectservice.util.JoinRequestProjectUtilsImpl;
import com.dev.vault.projectservice.util.ProjectUtils;
import com.dev.vault.projectservice.util.ProjectUtilsImpl;
import com.dev.vault.projectservice.util.RepositoryUtils;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.dev.vault.projectservice.model.enums.JoinStatus.PENDING;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;

/**
 * Service implementation of sending and managing Join Project Requests.
 */
@Slf4j
@Service
public class JoinProjectServiceImpl implements JoinProjectService {

    private final ProjectMembersRepository projectMembersRepository;
    private final ProjectRepository projectRepository;
    private final JoinProjectRepository joinProjectRepository;
    private final RepositoryUtils repositoryUtils;
    private final ProjectUtils projectUtils;
    private final JoinRequestProjectUtilsImpl joinProjectUtils;
    private final AuthUserFeignClient authFeignClient;
    private final HttpServletRequest httpServletRequest;


    /**
     * AllArgsConstructor with @Qualifier, since there are two beans of the same type (JoinRequestProjectUtilsImpl & ProjectUtilsImpl)
     */
    @Autowired
    public JoinProjectServiceImpl(
            ProjectMembersRepository projectMembersRepository, ProjectRepository projectRepository,
            JoinProjectRepository joinProjectRepository, RepositoryUtils repositoryUtils, AuthUserFeignClient authFeignClient,
            @Qualifier("projectUtilsImpl") ProjectUtilsImpl projectUtils,
            @Qualifier("joinRequestProjectUtilsImpl") JoinRequestProjectUtilsImpl joinProjectUtils,
            HttpServletRequest httpServletRequest
    ) {
        this.projectMembersRepository = projectMembersRepository;
        this.projectRepository = projectRepository;
        this.joinProjectRepository = joinProjectRepository;
        this.repositoryUtils = repositoryUtils;
        this.authFeignClient = authFeignClient;
        this.projectUtils = projectUtils;
        this.joinProjectUtils = joinProjectUtils;
        this.httpServletRequest = httpServletRequest;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public JoinResponse sendJoinRequest(Long projectId) {
        // Get the email of the current user
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        UserDTO currentUser = authFeignClient.getCurrentUsers_DTO(requestHeader);

        // Retrieve the project and user from the repositories
        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);

        // Check if the user is already a member of the project or has already sent a join project request for the project
        if (joinProjectUtils.isMemberOfProject(project, currentUser))
            throw new ResourceAlreadyExistsException("Ⓜ️👥 You are already a member of -" + project.getProjectName() + "- project 👥Ⓜ️", BAD_REQUEST, BAD_REQUEST.value());

        // Create a new join project request and save it to the repository
        joinProjectRepository.save(new JoinProject(project, currentUser.getUserId(), PENDING));

        // Return a JoinResponse indicating that the join project request was sent successfully and its status
        return JoinResponse.builder()
                .status("Join Project Request Sent successfully. Please wait until ProjectLeader approves your request :)")
                .joinStatus(PENDING)
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<JoinProjectResponse> getJoinRequestsByProjectIdAndPendingStatus(Long projectId, JoinStatus joinStatus) {
        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);

        // Check if the current user is the project leader or project admin of the project associated with the join request
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        UserDTO currentUser = authFeignClient.getCurrentUsers_DTO(requestHeader);
        if (projectUtils.isLeaderOrAdminOfProject(project, currentUser)) {

            // Retrieve the join requests from the repository and map them to JoinProjectDto objects
            return joinProjectRepository.findByProject_ProjectIdAndStatus(projectId, joinStatus)
                    .stream().map(joinRequest -> {
                                UserDTO user = authFeignClient.getUserDTOById(joinRequest.getUserId());

                                return JoinProjectResponse.builder()
                                        .projectName(joinRequest.getProject().getProjectName())
                                        .joinRequestId(joinRequest.getJoinRequestId())
                                        .joinRequestUsersEmail(user.getEmail())
                                        .joinStatus(joinRequest.getStatus())
                                        .build();
                            }
                    ).collect(Collectors.toList());
        } else {
            // Throw an exception if the user is not the project leader or admin of the project
            throw new NotLeaderOfProjectException("👮🏻 you are not the leader or admin of THIS project 👮🏻", FORBIDDEN, FORBIDDEN.value());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public JoinResponse updateJoinRequestStatus(Long joinProjectId, JoinStatus joinStatus) {
        // Find the joinProject with the given ID
        JoinProject joinProject = joinProjectRepository.findById(joinProjectId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have joinProject with {{}} in our db 😖", joinProjectId);
                    return new ResourceNotFoundException("JoinProject with the given ID was not found", NOT_FOUND);
                });

        // Check if the user is the leader or admin of the project
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        if (!projectUtils.isLeaderOrAdminOfProject(joinProject.getProject(), authFeignClient.getCurrentUsers_DTO(requestHeader)))
            throw new NotLeaderOfProjectException("👮🏻 you are not the leader or admin of this project 👮🏻");

        // Update the status of the joinProject
        joinProject.setStatus(joinStatus);
        joinProjectRepository.save(joinProject);

        // Perform actions based on the new join status
        switch (joinStatus) {
            case APPROVED -> {
                performJoinRequestApprovedActions(joinProject);
                return JoinResponse.builder()
                        .status("Approved successfully")
                        .joinStatus(joinStatus)
                        .build();
            }
            case REJECTED -> {
                performJoinRequestRejectedActions(joinProject);
                return JoinResponse.builder()
                        .status("Rejected successfully")
                        .joinStatus(joinStatus)
                        .build();
            }
            default -> {
                return null;
            }
        }
    }


    /**
     * Performs actions when a join request is approved.
     *
     * @param request The join request that was approved.
     */
    private void performJoinRequestApprovedActions(JoinProject request) {
        // Add the user to the project members
        ProjectMembers projectMembers = new ProjectMembers(request.getUserId(), request.getProject());
        projectMembersRepository.save(projectMembers);

        // Increment the member count of the project
        Project project = projectMembers.getProject();
        project.incrementMemberCount();
        projectRepository.save(project);

        // Delete the join request
        joinProjectRepository.delete(request);
    }


    /**
     * Performs actions when a join request is rejected.
     *
     * @param request The join request that was rejected.
     */
    private void performJoinRequestRejectedActions(JoinProject request) {
        // Delete the join request
        joinProjectRepository.delete(request);
    }

}
