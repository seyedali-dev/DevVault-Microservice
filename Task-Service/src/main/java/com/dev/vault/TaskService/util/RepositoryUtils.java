package com.dev.vault.TaskService.util;

import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.entity.TaskUser;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.TaskService.repository.TaskUserRepository;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.enums.TaskPriority;
import com.dev.vault.shared.lib.model.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepositoryUtils {

    private final TaskRepository taskRepository;
    private final TaskUserRepository taskUserRepository;
    private final ProjectUtilFeignClient projectUtilFeignClient;

    public Task find_TaskById_OrElseThrow_ResourceNotFoundException(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems the task with ID {{}} wasn't found in the db 😖", taskId);
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the project with  ID {" + taskId + "} wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }


    public ProjectDTO find_ProjectDTOById_OrElseThrow_ResourceNotFoundException(Long projectId) {
        return projectUtilFeignClient.getProjectAsDTO(projectId);
    }


    public Task find_TaskByTaskStatus_OrElseThrow_ResourceNotException(TaskStatus status) {
        return taskRepository.findByTaskStatus(status)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems the Task with Status {{}} wasn't found in the db 😖", status);
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the Task with Status {" + status + "} wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }


    public Task find_TaskByTaskPriority_OrElseThrow_ResourceNotException(TaskPriority priority) {
        return taskRepository.findByTaskPriority(priority)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems the Task with Priority {{}} wasn't found in the db 😖", priority);
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the Task with Priority {" + priority + "} wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }


    public Task find_TaskByProjectId_OrElseThrow_ResourceNotException(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems the Task with ProjectID {{}} wasn't found in the db 😖", projectId);
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the Task with ProjectID {" + projectId + "} wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }


    public TaskUser find_TaskUserByUserId_OrElseThrow_ResourceNotFoundException(long userId) {
        return taskUserRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems the TaskUser with UserID {{}} wasn't found in the db 😖", userId);
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the TaskUser with UserID '" + userId + "' wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }

}
