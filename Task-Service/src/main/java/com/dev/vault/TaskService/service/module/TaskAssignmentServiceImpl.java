package com.dev.vault.TaskService.service.module;

import com.dev.vault.TaskService.fegin.client.AuthUserFeignClient;
import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.entity.TaskUser;
import com.dev.vault.TaskService.model.request.AssignTaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.TaskService.service.interfaces.TaskAssignmentService;
import com.dev.vault.TaskService.util.RepositoryUtils;
import com.dev.vault.TaskService.util.TaskAssignmentUtils;
import com.dev.vault.TaskService.util.TaskUtils;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
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

    private final TaskRepository taskRepository;
    private final AuthUserFeignClient authUserFeignClient;
    private final ProjectUtilFeignClient projectUtilFeignClient;
    private final HttpServletRequest httpServletRequest;
    private final RepositoryUtils repositoryUtils;
    private final TaskAssignmentUtils taskAssignmentUtils;
    private final TaskUtils taskUtils;

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
        ProjectDTO projectDTO = repositoryUtils.find_ProjectDTOById_OrElseThrow_ResourceNotFoundException(projectId);

        // 2. Check if the task belongs to the project or throw a DevVaultException
        handle_TaskBelongingToProject(taskId, projectId, task);

        // 3. Check if the current user is leader/admin of the project
        handle_UserLeadership(projectDTO);

        Map<String, String> statusResponseMap = new HashMap<>();

        // 2. Loop through the list of user IDs and assign the task to them
        taskAssignmentUtils.assignTaskToUserList(task, projectId, userIdList, statusResponseMap);
        return taskAssignmentUtils.buildTaskResponse_AssignUsers(task, projectDTO, statusResponseMap);

    }

    private void handle_UserLeadership(ProjectDTO projectDTO) {
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUsersDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);
        if (!projectUtilFeignClient.isLeaderOrAdminOfProject(projectDTO.getProjectId(), currentUsersDTO_Id))
            throw new NotLeaderOfProjectException(
                    "👮🏻You are not a leader or admin of THIS project👮🏻",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );
    }

    private void handle_TaskBelongingToProject(long taskId, long projectId, Task task) {
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
    public TaskResponse assignTaskToAllUsersInProject(AssignTaskRequest assignTaskRequest) {
        // 1. Get the task & project from the request and the current user.
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(assignTaskRequest.getTaskId());
        ProjectDTO projectDTO = repositoryUtils.find_ProjectDTOById_OrElseThrow_ResourceNotFoundException(assignTaskRequest.getProjectId());

        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        // 2. Validate whether the task belongs to the project and whether the user is a member and leader/admin of the project.
        taskUtils.validateTaskAndProject(task, projectDTO.getProjectId(), currentUserDTO_Id);

        // 3. Create a responseMap to hold the responses for each user
        Map<String, String> responseMap = new HashMap<>();

        // 4. Retrieves a set of users associated with a task and a project, and updates the responseMap with the status of the assignment for each user.
        List<UserDTO> users = taskAssignmentUtils.getUsers(task.getTaskId(), projectDTO.getProjectId(), responseMap);

        // 5. Assign the task to all users in the set
        List<TaskUser> taskUsers = users.stream().map(userDTO -> TaskUser.builder()
                .userId(userDTO.getUserId())
                .task(task)
                .build()
        ).toList();
        task.setAssignedUsers(taskUsers);
        taskRepository.save(task);

        // 6. Build and return a TaskResponse with information about the assigned task and its assigned users
        return taskAssignmentUtils.buildTaskResponse_AssignUsers(task, projectDTO, responseMap);
    }

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
