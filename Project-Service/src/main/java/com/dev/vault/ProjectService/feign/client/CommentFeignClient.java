package com.dev.vault.ProjectService.feign.client;

import com.dev.vault.shared.lib.model.dto.CommentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "7-COMMENT-SERVICE/api/v1/inter-communication/comment", configuration = FeignClientConfiguration.class)
public interface CommentFeignClient {

    @GetMapping("/get-comments-as-dto-by-project-id/{projectId}")
    List<CommentDTO> getCommentListAsDTOs_ByProjectId(@PathVariable long projectId);

}
