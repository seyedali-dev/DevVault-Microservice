package com.dev.vault.ProjectService.service.module;

import com.dev.vault.ProjectService.exceptions.NotLeaderOfProjectException;
import com.dev.vault.ProjectService.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.ProjectService.exceptions.ResourceNotFoundException;
import com.dev.vault.ProjectService.model.dto.User;
import com.dev.vault.ProjectService.model.entity.JoinCoupon;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.repository.JoinCouponRepository;
import com.dev.vault.ProjectService.repository.ProjectRepository;
import com.dev.vault.ProjectService.service.RetrieveDataService;
import com.dev.vault.ProjectService.service.interfaces.JoinCouponService;
import com.dev.vault.ProjectService.util.ProjectUtils;
import com.dev.vault.ProjectService.util.RepositoryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

/**
 * Service implementation for generating join project request coupon.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JoinCouponServiceImpl implements JoinCouponService {

    private final JoinCouponRepository joinCouponRepository;
    private final ProjectRepository projectRepository;
    private final RetrieveDataService retrieveDataService;
    private final ProjectUtils projectUtils;
    private final RepositoryUtils repositoryUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String generateOneTimeJoinCoupon(Long projectId, Long requestingUserId) {
        // Get the project with the given ID from the database
        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);

        // Get the requesting user (the user who is requesting to join the project)
        User requestingUser = retrieveDataService.get_UserById_FromAuthenticationService(requestingUserId);

        // Check if the current user is the leader or admin of the specific project
        checkLeaderOrAdminOfProject(project, retrieveDataService.get_CurrentUser_FromAuthenticationService());

        // Check if a `JoinCoupon` has already been generated for the requesting user and project, and if so, throw an exception
        checkJoinCouponAlreadyGenerated(requestingUser, project);

        // Create a new `JoinCoupon` object with the requesting user, leader, and project
        String randomCoupon = generateRandomCoupon(project.getProjectName(), project.getMemberCount(), requestingUser);
        Long leader = project.getLeaderId();
        JoinCoupon joinCoupon = new JoinCoupon(requestingUser.getUserId(), leader, project, randomCoupon);

        joinCouponRepository.save(joinCoupon);

        return joinCoupon.getCoupon();
    }


    private void checkLeaderOrAdminOfProject(Project project, User user) {
        if (!projectUtils.isLeaderOrAdminOfProject(project, user))
            throw new NotLeaderOfProjectException("❌ You are not the leader or admin of this project ❌");
    }


    private void checkJoinCouponAlreadyGenerated(User requestingUser, Project project) {
        Optional<JoinCoupon> foundCoupon = joinCouponRepository.findByRequestingUserAndProject(requestingUser.getUserId(), project);
        if (foundCoupon.isPresent())
            throw new ResourceAlreadyExistsException("A coupon is already generated for: " + requestingUser.getUsername());
    }


    private String generateRandomCoupon(String projectName, int memberCount, User requestingUser) {
        // Get the project with the given name from the database
        Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "ProjectID", projectName));

        Random random = new Random();
        int paddedRandomNum = random.nextInt(91) + 10;

        // Append the random 4 characters of the project name to the coupon string
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(projectName.length());
            char randomChar = projectName.charAt(randomIndex);
            sb.append(randomChar);
        }

        // Append the project ID, leader ID, member count and requesting user's ID to the coupon string Return the generated coupon string
        return sb.toString().toUpperCase() +
               "_" +
               project.getProjectId() +
               retrieveDataService.get_UserById_FromAuthenticationService(project.getLeaderId()).getUserId() +
               "_" +
               memberCount +
               requestingUser.getUserId() +
               paddedRandomNum
                ;
    }

}
