//package com.dev.vault.projectservice.service.module;
//
//import com.dev.vault.projectservice.model.entity.JoinCoupon;
//import com.dev.vault.projectservice.model.entity.JoinProject;
//import com.dev.vault.projectservice.model.entity.Project;
//import com.dev.vault.projectservice.model.entity.ProjectMembers;
//import com.dev.vault.projectservice.model.enums.JoinStatus;
//import com.dev.vault.projectservice.model.response.JoinProjectResponse;
//import com.dev.vault.projectservice.model.response.JoinResponse;
//import com.dev.vault.projectservice.repository.JoinCouponRepository;
//import com.dev.vault.projectservice.repository.JoinProjectRepository;
//import com.dev.vault.projectservice.repository.ProjectMembersRepository;
//import com.dev.vault.projectservice.repository.ProjectRepository;
//import com.dev.vault.projectservice.service.interfaces.JoinProjectService;
//import com.dev.vault.projectservice.util.JoinRequestProjectUtilsImpl;
//import com.dev.vault.projectservice.util.ProjectUtils;
//import com.dev.vault.projectservice.util.ProjectUtilsImpl;
//import com.dev.vault.projectservice.util.RepositoryUtils;
//import com.dev.vault.shared.lib.exceptions.DevVaultException;
//import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
//import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
//import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static com.dev.vault.projectservice.model.enums.JoinStatus.PENDING;
//import static org.springframework.http.HttpStatus.FORBIDDEN;
//import static org.springframework.http.HttpStatus.NOT_FOUND;
//
///**
// * Service implementation of sending and managing Join Project Requests.
// */
//@Service
//@Slf4j
//public class JoinProjectServiceImpl implements JoinProjectService {
//
//    private final JoinCouponRepository joinCouponRepository;
//    private final ProjectMembersRepository projectMembersRepository;
//    private final ProjectRepository projectRepository;
//    private final JoinProjectRepository joinProjectRepository;
//    private final RetrieveDataService retrieveDataService;
//    private final RepositoryUtils repositoryUtils;
//    private final ProjectUtils projectUtils;
//    private final JoinRequestProjectUtilsImpl joinProjectUtils;
//
//
//    /**
//     * AllArgsConstructor with @Qualifier, since there are two beans of the same type (JoinRequestProjectUtilsImpl & ProjectUtilsImpl)
//     */
//    @Autowired
//    public JoinProjectServiceImpl(
//            JoinCouponRepository joinCouponRepository, ProjectMembersRepository projectMembersRepository, ProjectRepository projectRepository,
//            JoinProjectRepository joinProjectRepository, RetrieveDataService retrieveDataService, RepositoryUtils repositoryUtils,
//            @Qualifier("projectUtilsImpl") ProjectUtilsImpl projectUtils,
//            @Qualifier("joinRequestProjectUtilsImpl") JoinRequestProjectUtilsImpl joinProjectUtils
//    ) {
//        this.joinCouponRepository = joinCouponRepository;
//        this.projectMembersRepository = projectMembersRepository;
//        this.projectRepository = projectRepository;
//        this.joinProjectRepository = joinProjectRepository;
//        this.retrieveDataService = retrieveDataService;
//        this.repositoryUtils = repositoryUtils;
//        this.projectUtils = projectUtils;
//        this.joinProjectUtils = joinProjectUtils;
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    @Transactional
//    public JoinResponse sendJoinRequest(Long projectId, String joinCoupon) {
//        // Get the email of the current user
//        String email = retrieveDataService.get_CurrentUser_FromAuthenticationService().getEmail();
//
//        // Retrieve the project and user from the repositories
//        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);
//        User user = retrieveDataService.get_UserByEmail_FromAuthenticationService(email);
//
//        // Check if the user is already a member of the project or has already sent a join project request for the project
//        if (joinProjectUtils.isMemberOfProject(project, user))
//            throw new ResourceAlreadyExistsException("JoinRequest", "Member", email);
//
//        // Check if the JoinRequestCoupon is valid
//        if (!isCouponValid(project, joinCoupon))
//            throw new DevVaultException("Invalid JoinRequestCoupon");
//
//        // Mark the JoinRequestCoupon as used
//        JoinCoupon joinRequestCoupon = joinCouponRepository.findByCoupon(joinCoupon)
//                .orElseThrow(() -> new ResourceNotFoundException("JoinRequestCoupon", "Coupon", joinCoupon));
//        joinRequestCoupon.setUsed(true);
//        joinCouponRepository.save(joinRequestCoupon);
//
//        // Create a new join project request and save it to the repository
//        joinProjectRepository.save(new JoinProject(project, user.getUserId(), PENDING));
//
//        // Delete the JoinRequestCoupon if it has been used
//        if (joinRequestCoupon.isUsed())
//            joinCouponRepository.delete(joinRequestCoupon);
//
//        // Return a JoinResponse indicating that the join project request was sent successfully and its status
//        return JoinResponse.builder()
//                .status("Join Project Request Sent successfully. Please wait until ProjectLeader approves your request :)")
//                .joinStatus(PENDING)
//                .build();
//    }
//
//
//    /**
//     * Checks if the JoinRequestCoupon is valid for the specified project.
//     *
//     * @param project    the project to check the JoinRequestCoupon for
//     * @param joinCoupon the JoinRequestCoupon to check
//     * @return true if the JoinRequestCoupon is valid, false otherwise
//     * @throws DevVaultException         if the JoinRequestCoupon has been used or has exceeded its maximum usage count
//     * @throws ResourceNotFoundException if the JoinRequestCoupon cannot be found
//     */
//    private boolean isCouponValid(Project project, String joinCoupon) {
//        // Retrieve the project from the repository
//        repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(project.getProjectId());
//
//        // Check if the JoinRequestCoupon exists and if it is for the specific project and is for the requesting user (current user is requesting)
//        User currentUser = retrieveDataService.get_CurrentUser_FromAuthenticationService();
//        Optional<JoinCoupon> joinRequestCoupon = joinCouponRepository
//                .findByProjectAndRequestingUserAndCoupon(project, currentUser.getUserId(), joinCoupon);
//
//        if (joinRequestCoupon.isEmpty())
//            throw new DevVaultException("This JoinRequestCoupon is either; " +
//                                        "1. Not for this project: {" + project.getProjectName() + "}" +
//                                        " | 2. Not for this user: {" + currentUser.getUsername() + "}");
//
//        // Check if the JoinRequestCoupon has been used
//        if (joinRequestCoupon.get().isUsed())
//            throw new DevVaultException("You have already used this coupon. Please request for another one.");
//
//        return true;
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    @Transactional
//    public List<JoinProjectResponse> getJoinRequestsByProjectIdAndStatus(Long projectId, JoinStatus joinStatus) {
//        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);
//
//        // Check if the current user is the project leader or project admin of the project associated with the join request
//        User currentUser = retrieveDataService.get_CurrentUser_FromAuthenticationService();
//        if (projectUtils.isLeaderOrAdminOfProject(project, currentUser)) {
//
//            // Retrieve the join requests from the repository and map them to JoinProjectDto objects
//            return joinProjectRepository.findByProject_ProjectIdAndStatus(projectId, joinStatus)
//                    .stream().map(joinRequest -> {
//                                User user = retrieveDataService.get_UserById_FromAuthenticationService(joinRequest.getUserId());
//
//                                return JoinProjectResponse.builder()
//                                        .projectName(joinRequest.getProject().getProjectName())
//                                        .joinRequestId(joinRequest.getJoinRequestId())
//                                        .joinRequestUsersEmail(user.getEmail())
//                                        .joinStatus(joinRequest.getStatus())
//                                        .build();
//                            }
//                    ).collect(Collectors.toList());
//        } else {
//            // Throw an exception if the user is not the project leader or admin of the project
//            throw new NotLeaderOfProjectException("👮🏻 you are not the leader or admin of this project 👮🏻", FORBIDDEN, FORBIDDEN.value());
//        }
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    @Transactional
//    public JoinResponse updateJoinRequestStatus(Long joinProjectId, JoinStatus joinStatus) {
//        // Find the joinProject with the given ID
//        JoinProject joinProject = joinProjectRepository.findById(joinProjectId)
//                .orElseThrow(() -> {
//                    log.error("😖 huh... it seems we don't have joinProject with {{}} in our db 😖", joinProjectId);
//                    return new ResourceNotFoundException("JoinProject with the given ID was not found", NOT_FOUND);
//                });
//
//        // Check if the user is the leader or admin of the project
//        if (!projectUtils.isLeaderOrAdminOfProject(joinProject.getProject(), retrieveDataService.get_CurrentUser_FromAuthenticationService()))
//            throw new NotLeaderOfProjectException("👮🏻 you are not the leader or admin of this project 👮🏻");
//
//        // Update the status of the joinProject
//        joinProject.setStatus(joinStatus);
//        joinProjectRepository.save(joinProject);
//
//        // Perform actions based on the new join status
//        switch (joinStatus) {
//            case APPROVED -> {
//                performJoinRequestApprovedActions(joinProject);
//                return JoinResponse.builder()
//                        .status("Approved successfully")
//                        .joinStatus(joinStatus)
//                        .build();
//            }
//            case REJECTED -> {
//                performJoinRequestRejectedActions(joinProject);
//                return JoinResponse.builder()
//                        .status("Rejected successfully")
//                        .joinStatus(joinStatus)
//                        .build();
//            }
//            default -> {
//                return null;
//            }
//        }
//    }
//
//
//    /**
//     * Performs actions when a join request is approved.
//     *
//     * @param request The join request that was approved.
//     */
//    private void performJoinRequestApprovedActions(JoinProject request) {
//        // Add the user to the project members
//        ProjectMembers projectMembers = new ProjectMembers(request.getUserId(), request.getProject());
//        projectMembersRepository.save(projectMembers);
//
//        // Increment the member count of the project
//        Project project = projectMembers.getProject();
//        project.incrementMemberCount();
//        projectRepository.save(project);
//
//        // Delete the join request
//        joinProjectRepository.delete(request);
//    }
//
//
//    /**
//     * Performs actions when a join request is rejected.
//     *
//     * @param request The join request that was rejected.
//     */
//    private void performJoinRequestRejectedActions(JoinProject request) {
//        // Delete the join request
//        joinProjectRepository.delete(request);
//    }
//
//}
