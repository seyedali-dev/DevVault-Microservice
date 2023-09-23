package com.dev.vault.CommentService.util;

import com.dev.vault.CommentService.feign.client.ProjectUtilFeignClient;
import com.dev.vault.CommentService.feign.client.TaskUtilFeignClient;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Utility class for managing comments.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommentUtils {

    private final ProjectUtilFeignClient projectUtilFeignClient;
    private final TaskUtilFeignClient taskUtilFeignClient;

    /**
     * Validates whether the user is a member of the project.
     *
     * @param projectId the project to validate against
     * @param userId    the user to validate
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws ResourceNotFoundException   if the project is not found
     */
    public void validateProject_UserMembershipToProject(long projectId, long userId) {
        // 1. Check whether project exists or throw a ResourceNotFoundException.
        if (!projectUtilFeignClient.validateProjectExists(projectId)) {
            log.error("😖 huh... it seems the project with {{}} wasn't found in the db 😖", projectId);
            throw new ResourceNotFoundException(
                    "😖 huh... it seems the project with 'projectID: " + projectId + "' wasn't found in the db 😖",
                    NOT_FOUND,
                    NOT_FOUND.value()
            );
        }

        // 2. Check if the user is a member of the project or throw a NotMemberOfProjectException.
        if (!projectUtilFeignClient.isMemberOfProject(projectId, userId)) {
            log.error("👥Ⓜ️ You: {} are not a member of THIS project: {} Ⓜ️👥", userId, projectId);
            throw new NotMemberOfProjectException(
                    "👥Ⓜ️ You are not a member of THIS project Ⓜ️👥",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );
        }
    }


    public void validateTaskExistence(Long projectId, Long taskId) {
        // Check whether task exists or throw a ResourceNotException.
        if (!taskUtilFeignClient.validateTaskExistence(taskId)) {
            log.error("😖 huh... it seems the task with taskID {{}} wasn't found in the db 😖", projectId);
            throw new ResourceNotFoundException(
                    "😖 huh... it seems the task with 'taskID: " + taskId + "' wasn't found in the db 😖",
                    NOT_FOUND,
                    NOT_FOUND.value()
            );
        }
    }

}
