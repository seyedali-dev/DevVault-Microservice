package com.dev.vault.TaskService.service.module;

import com.dev.vault.TaskService.fegin.client.AuthUserFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.enums.TaskStatus;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.TaskService.service.interfaces.TaskProgressService;
import com.dev.vault.TaskService.util.RepositoryUtils;
import com.dev.vault.TaskService.util.TaskUtils;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Service implementation for task progress; marking a task as completed and updating its progress.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProgressServiceImpl implements TaskProgressService {

    private final TaskRepository taskRepository;
    private final TaskUtils taskUtils;
    private final RepositoryUtils repositoryUtils;
    private final AuthUserFeignClient authFeignClient;
    private final HttpServletRequest httpServletRequest;

    /**
     * {@inheritDoc}
     */
    @Override
    public void markTaskAsCompleted(Long taskId, Long projectId, TaskStatus taskStatus) {
        // 1. Find the `Task`, `ProjectDTO` and the current user which is a `UserDTO` (UserDTO & ProjectDTO are from Shared-Lib service)
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        long projectDTO_Id = repositoryUtils.findProjectDTOById_OrElseThrow_ResourceNotFoundException(projectId).getProjectId();

        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUser_Id = authFeignClient.getCurrentUsers_DTO(requestHeader).getUserId();

        // 2. Validate that the task belongs to the project and the user is a member or leader/admin of the project
        taskUtils.validateTaskAndProject(task, projectDTO_Id, currentUser_Id);

        // 3. Check if the task has already been completed and throw an exception if it has
        if (task.getTaskStatus().equals(TaskStatus.COMPLETED))
            throw new DevVaultException(
                    "Task has already been completed",
                    BAD_REQUEST,
                    BAD_REQUEST.value()
            );

        // 4. Mark the Task as Completed
        if (taskStatus.equals(TaskStatus.COMPLETED)) {
            // Set the boolean value indicating whether the task was overdue when it was completed
            task.setHasOverdue(task.getDueDate().isBefore(LocalDateTime.now()));
            task.setTaskStatus(taskStatus);
            task.setCompletionDate(LocalDateTime.now());
            taskRepository.save(task);
        } else
            throw new DevVaultException(
                    "TaskStatus should be only as `COMPLETED`",
                    BAD_REQUEST,
                    BAD_REQUEST.value()
            );
        // TODO: notify the users in that projectDTO (including leader and admin)
    }

}
