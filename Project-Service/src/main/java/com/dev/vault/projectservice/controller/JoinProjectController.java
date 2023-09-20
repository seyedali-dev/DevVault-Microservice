package com.dev.vault.ProjectService.controller;

import com.dev.vault.ProjectService.model.enums.JoinStatus;
import com.dev.vault.ProjectService.model.response.JoinProjectResponse;
import com.dev.vault.ProjectService.model.response.JoinResponse;
import com.dev.vault.ProjectService.service.interfaces.JoinProjectService;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dev.vault.ProjectService.model.enums.JoinStatus.*;

/**
 * REST controller for managing join project requests.
 */
@RestController
@RequestMapping("/api/v1/join_request")
@RequiredArgsConstructor
public class JoinProjectController {

    private final JoinProjectService joinRequestService;

    /**
     * Sends a join project request for the specified project.
     *
     * @param projectId  the ID of the project to send the join project request to
     * @param joinCoupon the coupon that project leader or project admin gave to user
     * @return ResponseEntity containing the JoinResponse object returned by the service
     */
    @PostMapping({"/{projectId}"})
    public ResponseEntity<JoinResponse> sendJoinRequest(
            @PathVariable Long projectId
    ) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        return ResponseEntity.ok(joinRequestService.sendJoinRequest(projectId));
    }


    /**
     * Retrieves all join project requests for the specified project with the specified status
     * i.e. {@link JoinStatus#PENDING}.
     *
     * @param projectId the ID of the project to retrieve join requests for
     * @return ResponseEntity containing a List of JoinRequest objects with the specified status
     */
    @GetMapping("/requests/{projectId}")
    public ResponseEntity<List<JoinProjectResponse>> getAllJoinRequestsByStatus(@PathVariable Long projectId)
            throws ResourceNotFoundException, NotLeaderOfProjectException {
        return ResponseEntity.ok(joinRequestService.getJoinRequestsByProjectIdAndPendingStatus(projectId, PENDING));
    }


    /**
     * Approves the specified join project request.
     *
     * @param joinRequestId the ID of the join project request to approve
     * @return ResponseEntity containing the JoinRequest object after it has been updated
     */
    @PostMapping("/{joinRequestId}/approve")
    public ResponseEntity<JoinResponse> approveJoinRequest(@PathVariable Long joinRequestId)
            throws ResourceNotFoundException, NotLeaderOfProjectException {
        return ResponseEntity.ok(joinRequestService.updateJoinRequestStatus(joinRequestId, APPROVED));
    }


    /**
     * Rejects the specified join project request.
     *
     * @param joinRequestId the ID of the join project request to reject
     * @return ResponseEntity containing the JoinRequest object after it has been updated
     */
    @PostMapping("/{joinRequestId}/reject")
    public ResponseEntity<JoinResponse> rejectJoinRequest(@PathVariable Long joinRequestId) {
        return ResponseEntity.ok(joinRequestService.updateJoinRequestStatus(joinRequestId, REJECTED));
    }

}
