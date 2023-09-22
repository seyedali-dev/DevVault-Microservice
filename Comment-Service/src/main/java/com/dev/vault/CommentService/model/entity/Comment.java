package com.dev.vault.CommentService.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(length = 10_000)
    private String comment;
    @CreationTimestamp
    private LocalDateTime commentedAt;

    /* relationships */
    @Column(name = "project_id")
    private long commentedOnProjectId;

    @Column(name = "task_id")
    private long commentedOnTaskId;

    @Column(name = "user_id")
    private long commentedByUserId;
    /* end of relationships */

}
