package com.dev.vault.CommentService.service.module;

import com.dev.vault.CommentService.feign.client.AuthUserFeignClient;
import com.dev.vault.CommentService.feign.client.ProjectUtilFeignClient;
import com.dev.vault.CommentService.feign.client.TaskUtilFeignClient;
import com.dev.vault.CommentService.model.entity.Comment;
import com.dev.vault.CommentService.repository.CommentRepository;
import com.dev.vault.CommentService.service.interfaces.CommentService;
import com.dev.vault.CommentService.util.CommentUtils;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.TaskDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Service implementation for managing comments on projects and tasks.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProjectUtilFeignClient projectUtilFeignClient;
    private final AuthUserFeignClient authUserFeignClient;
    private final TaskUtilFeignClient taskUtilFeignClient;
    private final CommentUtils commentUtils;
    private final HttpServletRequest httpServletRequest;

    /**
     * Adds a comment to the specified project.
     *
     * @param projectId the ID of the project to comment on
     * @param comment   the comment to add
     * @throws DevVaultException           if the project does not exist
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws NotLeaderOfProjectException if the user is not the leader or admin of the project
     */
    @Override
    @Transactional
    public void commentOnProject(Long projectId, String comment) {
        // 1. Find the project and the current user (logged-in user).
        long projectDTO_Id = projectUtilFeignClient.getProjectAsDTO(projectId).getProjectId();

        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        // 2. Validate that the user is a member and leader/admin of the project
        commentUtils.validateProject(projectDTO_Id, currentUserDTO_Id);

        Comment commentMade = new Comment();
        commentMade.setComment(comment);
        commentMade.setCommentedByUserId(currentUserDTO_Id);
        commentMade.setCommentedOnProjectId(projectDTO_Id);

        commentRepository.save(commentMade);
    }


    /**
     * Adds a comment to the specified task within a project.
     *
     * @param projectId the ID of the project containing the task
     * @param taskId    the ID of the task to comment on
     * @param comment   the comment to add
     * @throws DevVaultException           if the task or project does not exist
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws NotLeaderOfProjectException if the user is not the leader or admin of the project
     */
    @Override
    public void commentOnTask(Long projectId, Long taskId, String comment) {
        // 1. Find the task, project and the current user (logged-in user).
        TaskDTO foundTaskDTO = taskUtilFeignClient.getTaskDTO(taskId);
        ProjectDTO projectDTO = projectUtilFeignClient.getProjectAsDTO(projectId);

        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        // 2. Validate that the user is a member and leader/admin of the project
        taskUtilFeignClient.validateTaskAndProjectAndUser(foundTaskDTO.getTaskId(), projectDTO.getProjectId(), currentUserDTO_Id);

        Comment commentMade = new Comment();
        commentMade.setComment(comment);
        commentMade.setCommentedByUserId(currentUserDTO_Id);
        commentMade.setCommentedOnTaskId(foundTaskDTO.getTaskId());

        commentRepository.save(commentMade);
    }

}
