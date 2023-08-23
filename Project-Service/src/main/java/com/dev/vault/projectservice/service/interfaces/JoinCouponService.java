package com.dev.vault.projectservice.service.interfaces;

import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;

public interface JoinCouponService {

    /**
     * Generates a one-time join coupon for the specified project and requesting user.
     *
     * @param projectId        the ID of the project to generate the join coupon for
     * @param requestingUserId the ID of the user who is requesting to join the project
     * @return the generated join coupon string
     * @throws ResourceNotFoundException      if the project or user cannot be found
     * @throws NotLeaderOfProjectException    if the current user is not the leader or admin of the specific project
     * @throws ResourceAlreadyExistsException if a join coupon has already been generated for the requesting user and project
     */
    String generateOneTimeJoinCoupon(Long projectId, Long requestingUserId)
            throws ResourceNotFoundException, NotLeaderOfProjectException, ResourceAlreadyExistsException;

}
