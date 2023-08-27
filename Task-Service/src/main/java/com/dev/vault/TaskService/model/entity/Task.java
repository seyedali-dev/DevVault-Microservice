package com.dev.vault.TaskService.model.entity;

import com.dev.vault.TaskService.model.enums.TaskPriority;
import com.dev.vault.TaskService.model.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String taskName;
    private String description;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime completionDate;
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;
    private boolean hasOverdue;

    /* relationships */
    @OneToMany
    private List<TaskUser> assignedUsers = new ArrayList<>();
    private Long createdByUserId;
    private Long projectId;
    /* end of relationships */

}
