package com.dev.vault.CommentService.service.module;

import com.dev.vault.CommentService.feign.client.AuthUserFeignClient;
import com.dev.vault.CommentService.model.entity.Comment;
import com.dev.vault.CommentService.repository.CommentRepository;
import com.dev.vault.CommentService.service.interfaces.CommentService;
import com.dev.vault.CommentService.util.CommentUtils;
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
    private final AuthUserFeignClient authUserFeignClient;
    private final CommentUtils commentUtils;
    private final HttpServletRequest httpServletRequest;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void commentOnProject(long projectId, String comment) {
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        // 1. Validate the existence of project and the membership of current user (logged-in user) to project.
        commentUtils.validateProject_UserMembershipToProject(projectId, currentUserDTO_Id);

        // 2. Save the comment, projectId and userId that made the comment.
        Comment commentMade = new Comment();
        commentMade.setComment(comment);
        commentMade.setCommentedByUserId(currentUserDTO_Id);
        commentMade.setCommentedOnProjectId(projectId);

        commentRepository.save(commentMade);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void commentOnTask(Long projectId, Long taskId, String comment) {
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        // 1. Validate the existence of project and membership of current user (logged-in user).
        commentUtils.validateProject_UserMembershipToProject(projectId, currentUserDTO_Id);

        // 2. Validate the existence of task.
        commentUtils.validateTaskExistence(projectId, taskId);

        // 3. Save the comment, project, task and user ID.
        Comment commentMade = new Comment();
        commentMade.setComment(comment);
        commentMade.setCommentedByUserId(currentUserDTO_Id);
        commentMade.setCommentedOnProjectId(projectId);
        commentMade.setCommentedOnTaskId(taskId);

        commentRepository.save(commentMade);
    }

}
