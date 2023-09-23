package com.dev.vault.shared.lib.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long commentId;

    private String comment;
    private LocalDateTime commentedAt;

    private long commentedOnProjectId;
    private long commentedOnTaskId;
    private long commentedByUserId;

}
