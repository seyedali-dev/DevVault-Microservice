package com.dev.vault.TaskService.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks_users")
public class TaskUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskUserId;

    /* relationships */
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    private long userId;
    /* end of relationships */

}
