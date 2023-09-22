package com.dev.vault.CommentService.util;

import com.dev.vault.CommentService.feign.client.ProjectUtilFeignClient;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Utility class for managing comments.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommentUtils {

    private final ProjectUtilFeignClient projectUtilFeignClient;

    /**
     * Validates whether the user is a member and leader/admin of the project.
     *
     * @param projectId the project to validate against
     * @param userId    the user to validate
     * @throws DevVaultException           if the task does not belong to the project
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws NotLeaderOfProjectException if the user is not the leader or admin of the project
     */
    public void validateProject(long projectId, long userId) {
        // 1. Check if the user is a member of the project or throw a NotMemberOfProjectException
        if (!projectUtilFeignClient.isMemberOfProject(projectId, userId))
            throw new NotMemberOfProjectException(
                    "👥Ⓜ️ You are not a member of THIS project Ⓜ️👥",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );

        // 2. Check if the user is the leader or admin of the project or throw a NotLeaderOfProjectException
        if (!projectUtilFeignClient.isLeaderOrAdminOfProject(projectId, userId))
            throw new NotLeaderOfProjectException(
                    "❌👮🏻 You are not the leader or admin of THIS project 👮🏻❌",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );
    }

}
