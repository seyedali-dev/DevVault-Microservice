package com.dev.vault.CommentService.controller;

import com.dev.vault.CommentService.model.request.CommentRequest;
import com.dev.vault.CommentService.service.interfaces.CommentService;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.response.MapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * REST controller for commenting on a project or in a task.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    /**
     * Create a comment on a project.
     *
     * @param commentRequest a request class containing:
     *                       <ul>
     *                       <li>projectId-> the ID of the project</li>
     *                       <li>taskId-> the ID of the task</li>
     *                       <li>comment-> the comment field</li>
     *                       </ul>
     * @return a {@link MapResponse} object with the projectID and success message as a map
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws ResourceNotFoundException   if the project is not found
     */
    @PostMapping("/on-project")
    public ResponseEntity<MapResponse> commentOnProject(
            @RequestBody CommentRequest commentRequest
    ) throws NotMemberOfProjectException, ResourceNotFoundException {
        long projectId = commentRequest.getProjectId();
        String comment = commentRequest.getComment();

        commentService.commentOnProject(projectId, comment);
        HashMap<Object, Object> mapResponse = new HashMap<>();
        mapResponse.put("ProjectID: " + projectId, "Comment on PROJECT ✅");
        return new ResponseEntity<>(
                new MapResponse(mapResponse),
                CREATED
        );
    }


    /**
     * Create a comment on a task.
     *
     * @param commentRequest a request class containing:
     *                       <ul>
     *                       <li>projectId-> the ID of the project</li>
     *                       <li>taskId-> the ID of the task</li>
     *                       <li>comment-> the comment field</li>
     *                       </ul>
     * @return a {@link MapResponse} object with the taskID and success message as a map
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws ResourceNotFoundException   if the project or task is not found
     */
    @PostMapping("/on-task")
    public ResponseEntity<MapResponse> commentOnTask(
            @RequestBody CommentRequest commentRequest
    ) throws NotMemberOfProjectException, ResourceNotFoundException {
        long projectId = commentRequest.getProjectId();
        long taskId = commentRequest.getTaskId();
        String comment = commentRequest.getComment();

        commentService.commentOnTask(projectId, taskId, comment);
        HashMap<Object, Object> mapResponse = new HashMap<>();
        mapResponse.put("TaskID: " + taskId, "Comment on a TASK ✅");
        return new ResponseEntity<>(
                new MapResponse(mapResponse),
                CREATED
        );
    }

}