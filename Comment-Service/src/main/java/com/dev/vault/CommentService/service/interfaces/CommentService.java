package com.dev.vault.CommentService.service.interfaces;

/**
 * Service interface for managing comments on projects and tasks.
 */
public interface CommentService {

    /**
     * Adds a comment to the specified project.
     *
     * @param projectId the ID of the project to comment on
     * @param comment   the comment to add
     */
    void commentOnProject(Long projectId, String comment);


    /**
     * Adds a comment to the specified task within a project.
     *
     * @param projectId the ID of the project containing the task
     * @param taskId    the ID of the task to comment on
     * @param comment   the comment to add
     */
    void commentOnTask(Long projectId, Long taskId, String comment);

}
