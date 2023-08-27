//package com.dev.vault.TaskService.service.module;
//
//import com.dev.vault.helper.exception.DevVaultException;
//import com.dev.vault.helper.exception.NotLeaderOfProjectException;
//import com.dev.vault.helper.exception.NotMemberOfProjectException;
//import com.dev.vault.helper.exception.ResourceNotFoundException;
//import com.dev.vault.model.project.Project;
//import com.dev.vault.model.task.Task;
//import com.dev.vault.model.task.enums.TaskStatus;
//import com.dev.vault.model.user.User;
//import com.dev.vault.repository.task.TaskRepository;
//import com.dev.vault.service.interfaces.task.TaskProgressService;
//import com.dev.vault.service.interfaces.user.AuthenticationService;
//import com.dev.vault.util.repository.RepositoryUtils;
//import com.dev.vault.util.task.TaskUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//
///**
// * Service implementation for task progress; marking a task as completed and updating its progress.
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class TaskProgressServiceImpl implements TaskProgressService {
//    private final TaskRepository taskRepository;
//    private final TaskUtils taskUtils;
//    private final RepositoryUtils repositoryUtils;
//    private final AuthenticationService authenticationService;
//
//    /**
//     * Marks a task as completed and updates its status in the database. If the task has already been completed, a DevVaultException is thrown.
//     *
//     * @param taskId     the ID of the task to mark as completed.
//     * @param projectId  the ID of the project that the task belongs to.
//     * @param taskStatus the new status of the task (must be TaskStatus.COMPLETED).
//     * @throws DevVaultException           if the task has already been completed, or if the task status is not TaskStatus.COMPLETED.
//     * @throws ResourceNotFoundException   if the task or project with the given IDs cannot be found in the database.
//     * @throws NotMemberOfProjectException if the current user is not a member of the project with the given ID.
//     * @throws NotLeaderOfProjectException if the current user is not a leader or admin of the project with the given ID.
//     */
//    @Override
//    public void markTaskAsCompleted(Long taskId, Long projectId, TaskStatus taskStatus) {
//        Task task = repositoryUtils.findTaskById_OrElseThrow_ResourceNotFoundException(taskId);
//        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNoFoundException(projectId);
//        User currentUser = authenticationService.getCurrentUser();
//
//        // Validate that the task belongs to the project and the user is a member or leader/admin of the project
//        taskUtils.validateTaskAndProject(task, project, currentUser);
//
//        // Check if the task has already been completed and throw an exception if it has
//        if (task.getTaskStatus().equals(TaskStatus.COMPLETED))
//            throw new DevVaultException("Task has already been completed");
//
//        if (taskStatus.equals(TaskStatus.COMPLETED)) {
//            // Set the boolean value indicating whether the task was overdue when it was completed
//            task.setHasOverdue(task.getDueDate().isBefore(LocalDateTime.now()));
//            task.setTaskStatus(taskStatus);
//            task.setCompletionDate(LocalDateTime.now());
//            taskRepository.save(task);
//        } else
//            throw new DevVaultException("TaskStatus should be only as COMPLETED");
//        // TODO: notify the users in that project (including leader and admin)
//    }
//}
