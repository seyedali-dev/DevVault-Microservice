package com.dev.vault.TaskService.util;

import com.dev.vault.TaskService.fegin.client.AuthUserFeignClient;
import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.entity.TaskUser;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.TaskService.repository.TaskUserRepository;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Another utility class that provides helper methods for TaskAssigment to users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAssignmentUtils {

    private final TaskRepository taskRepository;
    private final AuthUserFeignClient authFeignClient;
    private final ProjectUtilFeignClient projectFeignClient;
    private final TaskUserRepository taskUserRepository;

    /**
     * Loops through the list of user IDs and assigns the task to each user.
     *
     * @param task       the task to assign
     * @param projectId  the ID of the project the task belongs to
     * @param userIdList the list of user IDs to assign the task to
     */
    public void assignTaskToUserList(
            Task task,
            Long projectId,
            List<Long> userIdList,
            Map<String, String> statusResponseMap
    ) {
        String notMemberMessage;
        String alreadyAssignedMessage;
        String successMessage;
        String userNotFoundMessage;

        for (Long userId : userIdList) {
            UserDTO userDTO;
            try {

                // 1. Find the user by ID or throw a RecourseNotFoundException
                userDTO = authFeignClient.getUserDTOById(userId);

                // 2. Check if the user is a member of the project, and add a response to the map
                boolean isMemberOfProject = projectFeignClient.isMemberOfProject(projectId, userDTO.getUserId());
                if (!isMemberOfProject) {
                    notMemberMessage = "❌😠😠 Fail: User with ID '" + userId + "' is NOT a member of THIS project with ID '" + projectId + "' 😠😠❌";
                    statusResponseMap.put(userDTO.getUsername(), notMemberMessage);
                    continue;
                }

                // 3. Check if the task is already assigned to the user; skip ahead and add a response to the map
                Optional<Task> isTaskAlreadyAssigned = taskRepository.findByAssignedUsers_UserIdAndTaskId(userDTO.getUserId(), task.getTaskId());
                if (isTaskAlreadyAssigned.isPresent()) {
                    alreadyAssignedMessage = "❌😖 Fail: Task is already assigned to user '" + userDTO.getUsername() + "' 😖❌";
                    statusResponseMap.put(userDTO.getUsername(), alreadyAssignedMessage);
                    continue;
                }

                // 4. Assign the user to the task, build a task_user object for managing relationship and add a response to the map
                TaskUser newTaskUser = TaskUser.builder()
                        .userId(userDTO.getUserId())
                        .task(task)
                        .build();
                task.getAssignedUsers().add(newTaskUser);
                taskUserRepository.save(newTaskUser);

                successMessage = "✅ Success: Task assigned to user '" + userDTO.getUsername() + "' ✅";
                statusResponseMap.put(userDTO.getUsername(), successMessage);

                // Set the assigned users for the task and save the task
                task.setAssignedUsers(task.getAssignedUsers());
                taskRepository.save(task);

            } catch (Exception e) {
                userNotFoundMessage = "❌😶😐🙄 User with ID '" + userId + "' was not Found 🙄😐😶❌";
                statusResponseMap.put(userId.toString(), userNotFoundMessage);
            }
        }
    }


    /**
     * Builds a TaskResponse object with information about the assigned task and its assigned users.
     *
     * @param task             the assigned task
     * @param projectDTO       the project the task belongs to
     * @param assignedUsersMap the map of responses for each assigned user
     * @return a TaskResponse object with information about the assigned task and its assigned users
     */
    public TaskResponse buildTaskResponse_AssignUsers(
            Task task,
            ProjectDTO projectDTO,
            Map<String, String> assignedUsersMap
    ) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setTaskStatus(task.getTaskStatus());
        taskResponse.setDueDate(task.getDueDate());
        taskResponse.setProjectName(projectDTO.getProjectName());
        taskResponse.setAssignedUsers(assignedUsersMap);
        return taskResponse;
    }


    /**
     * Retrieves a set of users associated with a task and a project.
     *
     * @param projectId         The project to which the task belongs.
     * @return A set of users associated with the task and the project.
     */
    public List<UserDTO> getUsers(
            long projectId
    ) {
        return projectFeignClient.getUsersAssociatedWithTaskAndProject(projectId);
    }

}
