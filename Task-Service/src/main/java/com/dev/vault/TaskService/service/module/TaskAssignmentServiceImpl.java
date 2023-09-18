package com.dev.vault.TaskService.service.module;

import com.dev.vault.TaskService.fegin.client.AuthUserFeignClient;
import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.request.AssignTaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.service.interfaces.TaskAssignmentService;
import com.dev.vault.TaskService.util.RepositoryUtils;
import com.dev.vault.TaskService.util.TaskAssignmentUtils;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Service implementation for task assignments.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final AuthUserFeignClient authUserFeignClient;
    private final ProjectUtilFeignClient projectUtilFeignClient;
    private final TaskAssignmentUtils taskAssignmentUtils;
    private final RepositoryUtils repositoryUtils;
    private final HttpServletRequest httpServletRequest;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TaskResponse assignTaskToUsers(AssignTaskRequest assignTaskRequest) {
        long taskId = assignTaskRequest.getTaskId();
        long projectId = assignTaskRequest.getProjectId();
        List<Long> userIdList = assignTaskRequest.getUserIdList();

        // 1. Extract the task and project using the assignTaskRequest class
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        ProjectDTO projectDTO = repositoryUtils.findProjectDTOById_OrElseThrow_ResourceNotFoundException(projectId);

        // 2. Check if the task belongs to the project or throw a DevVaultException
        handleTaskBelongingToProject(taskId, projectId, task);

        // 3. Check if the current user is leader/admin of the project
        handleUserLeadership(projectDTO);

        Map<String, String> statusResponseMap = new HashMap<>();

        // 2. Loop through the list of user IDs and assign the task to them
        taskAssignmentUtils.assignTaskToUserList(task, projectId, userIdList, statusResponseMap);
        return taskAssignmentUtils.buildTaskResponse_AssignUsers(task, projectDTO, statusResponseMap);

    }

    private void handleUserLeadership(ProjectDTO projectDTO) {
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUsersDTO_Id = authUserFeignClient.getCurrentUsers_DTO(requestHeader).getUserId();
        if (!projectUtilFeignClient.isLeaderOrAdminOfProject(projectDTO.getProjectId(), currentUsersDTO_Id))
            throw new NotLeaderOfProjectException(
                    "👮🏻You are not a leader or admin of THIS project👮🏻",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );
    }

    private void handleTaskBelongingToProject(long taskId, long projectId, Task task) {
        if (!task.getProjectId().equals(projectId))
            throw new DevVaultException(
                    "Task with ID '" + taskId + "' does not belong to project with ID '" + projectId + "'",
                    BAD_REQUEST,
                    BAD_REQUEST.value()
            );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResponse assignTaskToAllUsersInProject(Long taskId, Long projectId) {
        return null;
    }
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
//        User currentUser = authUserFeignClient.getCurrentUser();
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

    @Override
    public void unAssignTaskFromUser(Long taskId, Long projectId, Long userId) {
        //TODO
    }


    @Override
    public void unAssignTaskFromUsers(Long taskId, Long projectId, List<Long> userIdList) {
        //TODO
    }


    @Override
    public void unassignTaskFromAllUsersInProject(Long taskId, Long projectId) {
        //TODO
    }

}
