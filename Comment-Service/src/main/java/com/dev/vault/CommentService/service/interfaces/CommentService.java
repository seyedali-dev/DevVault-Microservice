package com.dev.vault.CommentService.service.interfaces;

import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;

/**
 * Service interface for managing comments on projects and tasks.
 */
public interface CommentService {

    /**
     * Adds a comment to the specified project.
     *
     * @param projectId the ID of the project to comment on
     * @param comment   the comment to add
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws ResourceNotFoundException   if the project is not found
     */
    void commentOnProject(long projectId, String comment)
            throws NotMemberOfProjectException, ResourceNotFoundException;


    /**
     * Adds a comment to the specified task within a project.
     *
     * @param projectId the ID of the project containing the task
     * @param taskId    the ID of the task to comment on
     * @param comment   the comment to add
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws ResourceNotFoundException   if the task or project is not found
     */
    void commentOnTask(Long projectId, Long taskId, String comment)
            throws NotMemberOfProjectException, ResourceNotFoundException;

}
