package com.dev.vault.CommentService.controller;

import com.dev.vault.CommentService.service.interfaces.CommentService;
import com.dev.vault.shared.lib.model.response.MapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Handles a POST request to create a comment on a project.
     *
     * @param projectId The ID of the project to comment on.
     * @param comment   The text of the comment.
     * @return A ResponseEntity with a success message and HTTP status code 201 (CREATED).
     */
    @PostMapping("/project/{projectId}")
    public ResponseEntity<MapResponse> commentOnProject(
            @PathVariable Long projectId,
            @RequestBody String comment
    ) {
        commentService.commentOnProject(projectId, comment);
        HashMap<Object, Object> mapResponse = new HashMap<>();
        mapResponse.put(projectId, "Comment on PROJECT ✅");
        return new ResponseEntity<>(
                new MapResponse(mapResponse),
                CREATED
        );
    } // TODO:: test this method out!!!


    /**
     * Handles a POST request to create a comment on a task within a project.
     *
     * @param projectId The ID of the project containing the task to comment on.
     * @param taskId    The ID of the task to comment on.
     * @param comment   The text of the comment.
     * @return A ResponseEntity with a success message and HTTP status code 201 (CREATED).
     */
    @PostMapping("/project/{projectId}/task/{taskId}")
    public ResponseEntity<MapResponse> commentOnTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody String comment
    ) {
        commentService.commentOnTask(projectId, taskId, comment);
        HashMap<Object, Object> mapResponse = new HashMap<>();
        mapResponse.put(projectId, "Comment on a TASK ✅");
        return new ResponseEntity<>(
                new MapResponse(mapResponse),
                CREATED
        );
    } // TODO:: test this method out!!!

}