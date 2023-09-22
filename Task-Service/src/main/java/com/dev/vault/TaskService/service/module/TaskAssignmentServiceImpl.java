package com.dev.vault.TaskService.service.module;

import com.dev.vault.TaskService.fegin.client.AuthUserFeignClient;
import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.entity.TaskUser;
import com.dev.vault.TaskService.model.request.AssignTaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.TaskService.repository.TaskUserRepository;
import com.dev.vault.TaskService.service.interfaces.TaskAssignmentService;
import com.dev.vault.TaskService.util.ProjectTaskValidationUtils;
import com.dev.vault.TaskService.util.RepositoryUtils;
import com.dev.vault.TaskService.util.TaskAssignmentUtils;
import com.dev.vault.TaskService.util.TaskUtils;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import com.dev.vault.shared.lib.model.response.MapResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Service implementation for task assignments.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    private final TaskRepository taskRepository;

    private final TaskUserRepository taskUserRepository;
    private final AuthUserFeignClient authUserFeignClient;
    private final ProjectUtilFeignClient projectUtilFeignClient;
    private final HttpServletRequest httpServletRequest;
    private final RepositoryUtils repositoryUtils;
    private final TaskUtils taskUtils;
    private final TaskAssignmentUtils taskAssignmentUtils;
    private final ProjectTaskValidationUtils taskValidationUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TaskResponse assignTaskToUser_s(AssignTaskRequest assignTaskRequest) {
        long taskId = assignTaskRequest.getTaskId();
        long projectId = assignTaskRequest.getProjectId();
        List<Long> userIdList = assignTaskRequest.getUserIdList();

        // 1. Extract the task and project using the assignTaskRequest class
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        ProjectDTO projectDTO = repositoryUtils.find_ProjectDTOById_OrElseThrow_ResourceNotFoundException(projectId);

        // 2. Check if the task belongs to the project or throw a DevVaultException
        taskValidationUtils.handle_TaskBelongingToProject(task, projectId);

        // 3. Check if the current user is leader/admin of the project
        taskValidationUtils.handle_UserLeadership(projectDTO);

        Map<String, String> statusResponseMap = new HashMap<>();

        // 2. Loop through the list of user IDs and assign the task to them
        taskAssignmentUtils.assignTaskToUserList(task, projectId, userIdList, statusResponseMap);
        return taskAssignmentUtils.buildTaskResponse_AssignUsers(task, projectDTO, statusResponseMap);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TaskResponse assignTaskToAllUsersInProject(AssignTaskRequest assignTaskRequest) {
        // 1. Get the task & project from the request and the current user.
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(assignTaskRequest.getTaskId());
        ProjectDTO projectDTO = repositoryUtils.find_ProjectDTOById_OrElseThrow_ResourceNotFoundException(assignTaskRequest.getProjectId());

        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        // 2. Validate whether the task belongs to the project and whether the user is a member and leader/admin of the project.
        taskUtils.validateTaskAndProject(task, projectDTO.getProjectId(), currentUserDTO_Id);

        // 3. Create a responseMap to hold the responses for each user.
        Map<String, String> responseMap = new HashMap<>();

        // 4. Retrieves a set of users associated with a task and a project.
        List<UserDTO> members = taskAssignmentUtils.getUsers(projectDTO.getProjectId());

        // 5. Assign the task to all users in the list
        assignTaskToAllUsersInTheList(task, responseMap, members);

        // 6. Build and return a TaskResponse with information about the assigned task and its assigned users
        return taskAssignmentUtils.buildTaskResponse_AssignUsers(task, projectDTO, responseMap);
    }

    private void assignTaskToAllUsersInTheList(Task task, Map<String, String> responseMap, List<UserDTO> members) {
        AtomicReference<String> alreadyAssignedMessage = new AtomicReference<>();
        AtomicReference<String> successMessage = new AtomicReference<>();
        members.forEach(memberDTO -> {

            alreadyAssignedMessage.set("❌😖 Fail: Task is already assigned to user '" + memberDTO.getUsername() + "' 😖❌");
            successMessage.set("✅ Success: Task assigned to user '" + memberDTO.getUsername() + "' ✅");

            // check if the task is already assigned to the user; skip ahead, and add a response to the map
            boolean isTaskAlreadyAssigned = taskUserRepository.existsByUserIdAndTask_TaskId(memberDTO.getUserId(), task.getTaskId());
            if (isTaskAlreadyAssigned)
                responseMap.put(memberDTO.getUsername(), String.valueOf(alreadyAssignedMessage));

            else {
                responseMap.put(memberDTO.getUsername(), String.valueOf(successMessage));

                TaskUser taskUser = TaskUser.builder()
                        .task(task)
                        .userId(memberDTO.getUserId())
                        .build();
                taskUserRepository.save(taskUser);
            }

        });
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MapResponse unAssignTaskFromUser_s(AssignTaskRequest assignTaskRequest) {
        long projectId = assignTaskRequest.getProjectId();
        long taskId = assignTaskRequest.getTaskId();
        List<Long> userIdList = assignTaskRequest.getUserIdList();

        // 1. Find the task and validate user leadership and task belonging to project.
        validateUserLeadershipAndProjectBelonging(taskId, projectId);
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);

        // 2. Unassign the task from the user(s) and return a map as response.
        return unAssignTaskFromUsersList(userIdList, new MapResponse(), task);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MapResponse unassignTaskFromAllUsersInProject(AssignTaskRequest assignTaskRequest) {
        long projectId = assignTaskRequest.getProjectId();
        long taskId = assignTaskRequest.getTaskId();

        // 1. Find the task and validate user leadership and task belonging to project.
        validateUserLeadershipAndProjectBelonging(taskId, projectId);
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);

        // 2. Find the UserIDs of the assignedUsers to the task.
        List<Long> assignedUser_sToTask = taskUserRepository.findByTask(task)
                .stream()
                .map(TaskUser::getUserId)
                .toList();

        // 3. Unassign the task from the user(s) and return a map as response.
        return unAssignTaskFromUsersList(assignedUser_sToTask, new MapResponse(), task);
    }


    private void validateUserLeadershipAndProjectBelonging(long taskId, long projectId) {
        ProjectDTO projectDTO = projectUtilFeignClient.getProjectAsDTO(projectId);
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);

        // a) Check if the user attempting (logged-in user) to unassign is a leader/admin of project.
        taskValidationUtils.handle_UserLeadership(projectDTO);

        // b) Check whether task belongs to the project.
        taskValidationUtils.handle_TaskBelongingToProject(task, projectDTO.getProjectId());
    }

    private MapResponse unAssignTaskFromUsersList(List<Long> userIdList, MapResponse mapResponse, Task task) {
        String memberNotPresentMessage = "😊 It seems no member was found that the task '" + task.getTaskName() + "' is assigned 😊";
        if (userIdList.isEmpty())
            mapResponse.getMapResponse().put("NoMemberFound?", memberNotPresentMessage);

        userIdList.forEach(userId -> {
            // a) find the user
            UserDTO userDTO = authUserFeignClient.getUserDTOById(userId);
            String userDTO_Username = userDTO.getUsername();

            // b) find the task associated with the user and unassign (delete) it if it is present
            Optional<TaskUser> assignedTaskToUserToRemove = taskUserRepository.findByUserId(userDTO.getUserId());
            if (assignedTaskToUserToRemove.isPresent()) {
                // c) remove association of taskUser from task
                task.getAssignedUsers().remove(assignedTaskToUserToRemove.get());
                taskRepository.save(task);

                // d) now delete the TaskUser
                taskUserRepository.delete(assignedTaskToUserToRemove.get());

                String unassignSuccessMessage = "Task '" + task.getTaskName() + "' UnAssigned from User '" + userDTO_Username + "' ✅";
                mapResponse.getMapResponse().put(userDTO_Username, unassignSuccessMessage);
            } else {
                String memberNotFoundErrorMessage = "😊 huh... It seems user '" + userDTO_Username + "' either was already unassigned, or is not a member of this project 😊";
                mapResponse.getMapResponse().put(userDTO_Username, memberNotFoundErrorMessage);
            }
        });
        return mapResponse;
    }

}
