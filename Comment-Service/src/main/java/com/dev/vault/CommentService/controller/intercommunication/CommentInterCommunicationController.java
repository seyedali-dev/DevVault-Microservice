package com.dev.vault.CommentService.controller.intercommunication;

import com.dev.vault.CommentService.service.intercommunication.CommentInterCommunicationService;
import com.dev.vault.shared.lib.model.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inter-communication/comment")
public class CommentInterCommunicationController {

    private final CommentInterCommunicationService interCommunicationService;

    /**
     * Retrieves the comments of a project and maps it to {@link CommentDTO}.
     *
     * @param projectId the comments of the project that we want to retrieve
     * @return list of comments as {@link CommentDTO} of a project; If no comment was found an empty list will be returned
     */
    @GetMapping("/get-comments-as-dto-by-project-id/{projectId}")
    public List<CommentDTO> getCommentListAsDTOs_ByProjectId(@PathVariable long projectId) {
        return interCommunicationService.getCommentListAsDTOs_ByProjectId(projectId);
    }

}
