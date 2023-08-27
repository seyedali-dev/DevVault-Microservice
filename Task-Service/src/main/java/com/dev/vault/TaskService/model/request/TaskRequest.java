package com.dev.vault.TaskService.model.request;

import com.dev.vault.TaskService.model.enums.TaskPriority;
import com.dev.vault.TaskService.model.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    private String taskName;
    private String description;
    @FutureOrPresent
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;

}
