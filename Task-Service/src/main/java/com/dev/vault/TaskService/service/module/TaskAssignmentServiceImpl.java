//package com.dev.vault.TaskService.service.module;
//
//import com.dev.vault.helper.exception.DevVaultException;
//import com.dev.vault.helper.exception.NotLeaderOfProjectException;
//import com.dev.vault.helper.exception.NotMemberOfProjectException;
//import com.dev.vault.helper.exception.ResourceAlreadyExistsException;
//import com.dev.vault.helper.payload.task.TaskResponse;
//import com.dev.vault.model.project.Project;
//import com.dev.vault.model.task.Task;
//import com.dev.vault.model.user.User;
//import com.dev.vault.repository.task.TaskRepository;
//import com.dev.vault.service.interfaces.task.TaskAssignmentService;
//import com.dev.vault.service.interfaces.user.AuthenticationService;
//import com.dev.vault.util.project.ProjectUtils;
//import com.dev.vault.util.repository.RepositoryUtils;
//import com.dev.vault.util.task.TaskUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Service implementation for task assignments.
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class TaskAssignmentServiceImpl implements TaskAssignmentService {
//
//    private final TaskRepository taskRepository;
//    private final AuthenticationService authenticationService;
//    private final ProjectUtils projectUtils;
//    private final TaskUtils taskUtils;
//    private final RepositoryUtils repositoryUtils;
//
//    /**
//     * Assigns a task to a list of users.
//     *
//     * @param taskId     The ID of the task to assign.
//     * @param projectId  The ID of the project to which the task belongs.
//     * @param userIdList The list of user IDs to assign the task to.
//     * @return A {@link TaskResponse} containing information about the assigned task and its assigned users.
//     * @throws RecourseNotFoundException      If the task or project is not found.
//     * @throws DevVaultException              If the task does not belong to the project.
//     * @throws NotLeaderOfProjectException    If the current user is not a leader or admin of the project.
//     * @throws ResourceAlreadyExistsException If the task is already assigned to a user.
//     * @throws NotMemberOfProjectException    If the user is not a member of the project.
//     */
//    @SuppressWarnings("JavadocReference")
//    @Override
//    @Transactional
//    public TaskResponse assignTaskToUsers(Long taskId, Long projectId, List<Long> userIdList) {
//        Task task = repositoryUtils.findTaskById_OrElseThrow_ResourceNotFoundException(taskId);
//        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNoFoundException(projectId);
//        // Check if the task belongs to the project or throw a DevVaultException if it doesn't
//        if (!task.getProject().getProjectId().equals(projectId))
//            throw new DevVaultException("Task with ID " + taskId + " does not belong to project with ID " + projectId);
//
//        User currentUser = authenticationService.getCurrentUser();
//        if (!projectUtils.isLeaderOrAdminOfProject(project, currentUser))
//            throw new NotLeaderOfProjectException("👮🏻You are not a leader or admin of this project👮🏻");
//
//        Map<String, String> statusResponseMap = new HashMap<>();
//
//        // Loop through the list of user IDs and assign the task to them
//        taskUtils.assignTaskToUserList(projectId, userIdList, task, project, statusResponseMap);
//        return taskUtils.buildTaskResponse(task, project, statusResponseMap);
//    }
//
//    /**
//     * Assigns a task to all users in a project.
//     *
//     * @param taskId    The ID of the task to assign.
//     * @param projectId The ID of the project to which the task belongs.
//     * @return A {@link TaskResponse} containing information about the assigned task and its assigned users.
//     * @throws RecourseNotFoundException   If the task or project is not found.
//     * @throws NotLeaderOfProjectException If the current user is not a leader or admin of the project.
//     * @throws NotMemberOfProjectException If the user is not a member of the project.
//     */
//    @SuppressWarnings("JavadocReference")
//    @Override
//    @Transactional
//    public TaskResponse assignTaskToAllUsersInProject(Long taskId, Long projectId) {
//        Task task = repositoryUtils.findTaskById_OrElseThrow_ResourceNotFoundException(taskId);
//        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNoFoundException(projectId);
//        User currentUser = authenticationService.getCurrentUser();
//
//        // Validate task and project
//        taskUtils.validateTaskAndProject(task, project, currentUser);
//
//        // Create a responseMap to hold the responses for each user
//        Map<String, String> responseMap = new HashMap<>();
//        // Retrieves a set of users associated with a task and a project, and updates the responseMap with the status of the assignment for each user.
//        Set<User> users = taskUtils.getUsers(task, project, responseMap);
//        // Assign the task to all users in the set
//        task.setAssignedUsers(users);
//        taskRepository.save(task);
//
//        // Build and return a TaskResponse with information about the assigned task and its assigned users
//        return taskUtils.buildTaskResponse(task, project, responseMap);
//    }
//
//    @Override
//    public void unAssignTaskFromUser(Long taskId, Long projectId, Long userId) {
//        //TODO
//    }
//
//    @Override
//    public void unAssignTaskFromUsers(Long taskId, Long projectId, List<Long> userIdList) {
//        //TODO
//    }
//
//    @Override
//    public void unassignTaskFromAllUsersInProject(Long taskId, Long projectId) {
//        //TODO
//    }
//}
