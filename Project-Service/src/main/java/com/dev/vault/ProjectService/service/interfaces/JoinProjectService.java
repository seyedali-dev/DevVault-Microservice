package com.dev.vault.ProjectService.service.interfaces;

import com.dev.vault.ProjectService.exceptions.NotLeaderOfProjectException;
import com.dev.vault.ProjectService.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.ProjectService.exceptions.ResourceNotFoundException;
import com.dev.vault.ProjectService.model.enums.JoinStatus;
import com.dev.vault.ProjectService.model.response.JoinProjectResponse;
import com.dev.vault.ProjectService.model.response.JoinResponse;

import java.util.List;

public interface JoinProjectService {

    /**
     * Sends a join project request for the specified project on behalf of the current user. All users are allowed.
     * Only allowed if the user has 'JoinToken' that the project leader, or project admin generated.
     *
     * @param projectId  the ID of the project to send the join project request to
     * @param joinCoupon the coupon that the leader or admin gave to user
     * @return JoinResponse indicating whether the join project request was sent successfully and its status
     * @throws ResourceAlreadyExistsException if the user is already a member of the project or has already sent a join project request for the project
     * @throws ResourceNotFoundException      if the project or user cannot be found
     */
    JoinResponse sendJoinRequest(Long projectId, String joinCoupon)
            throws ResourceAlreadyExistsException, ResourceNotFoundException;


    /**
     * Retrieves a list of all join project requests for the specified project with the specified status.
     * Only project leader and project admin are allowed.
     *
     * @param projectId  the ID of the project to retrieve join project requests for
     * @param joinStatus the status of the join requests to retrieve
     * @return a List of JoinProjectDto objects containing information about each join request
     */
    List<JoinProjectResponse> getJoinRequestsByProjectIdAndStatus(Long projectId, JoinStatus joinStatus)
            throws ResourceNotFoundException, NotLeaderOfProjectException;


    /**
     * Updates the status of a join request with the given ID and join status (APPROVED, REJECTED).
     *
     * @param joinProjectId The ID of the join request to update.
     * @param joinStatus    The new status of the join request.
     * @return A JoinResponse object with the updated join status.
     * @throws ResourceNotFoundException   If the join request with the given ID is not found.
     * @throws NotLeaderOfProjectException If the user is not the leader or admin of the project.
     */
    JoinResponse updateJoinRequestStatus(Long joinProjectId, JoinStatus joinStatus)
            throws ResourceNotFoundException, NotLeaderOfProjectException;

}
