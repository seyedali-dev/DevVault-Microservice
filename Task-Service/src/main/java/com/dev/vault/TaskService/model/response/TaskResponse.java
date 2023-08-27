package com.dev.vault.TaskService.model.response;

import com.dev.vault.TaskService.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private String taskName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dueDate;
    private String projectName;
    private TaskStatus taskStatus;
    private Map<String, String> assignedUsers;

}
