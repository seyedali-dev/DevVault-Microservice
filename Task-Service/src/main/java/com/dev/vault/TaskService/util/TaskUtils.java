package com.dev.vault.TaskService.util;

import com.dev.vault.TaskService.fegin.client.AuthUserFeignClient;
import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.request.TaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * A utility class that provides helper methods for working with tasks.
 * This class contains method for checking if a task with the same name already exists in a project. //TODO
 */
@Service
@RequiredArgsConstructor
public class TaskUtils {

    private final TaskRepository taskRepository;
    private final AuthUserFeignClient authFeignClient;
    private final ProjectUtilFeignClient projectFeignClient;

    /**
     * Checks if a task with the same name already exists in the project
     *
     * @param taskRequest the request object containing the details of the task to create
     * @param projectId   the project to check for existing tasks
     * @return true if a task with the same name already exists in the project, false otherwise
     */
    public boolean doesTaskAlreadyExists(TaskRequest taskRequest, Long projectId) {
        Optional<Task> foundTask = taskRepository.findByProjectIdAndTaskName(projectId, taskRequest.getTaskName());
        return foundTask.isPresent();
    }


    /**
     * Validates whether the task belongs to the project and whether the user is a member and leader/admin of the project.
     *
     * @param task      the task to validate
     * @param projectId the project to validate against
     * @param userId    the user to validate
     * @throws DevVaultException           if the task does not belong to the project
     * @throws NotMemberOfProjectException if the user is not a member of the project
     * @throws NotLeaderOfProjectException if the user is not the leader or admin of the project
     */
    public void validateTaskAndProject(Task task, long projectId, long userId) {
        // 1. Check if the task belongs to the project or throw a DevVaultException
        if (!task.getProjectId().equals(projectId))
            throw new DevVaultException(
                    "😖 Task {" + task.getTaskName() + "} does not belong to project {" + projectId + "} 😖",
                    EXPECTATION_FAILED,
                    EXPECTATION_FAILED.value()
            );

        // 2. Check if the user is a member of the project or else throw a NotMemberOfProjectException
        long userDTO_Id = authFeignClient.getUserDTOById(userId).getUserId();
        if (!projectFeignClient.isMemberOfProject(projectId, userDTO_Id))
            throw new NotMemberOfProjectException(
                    "Ⓜ️👥 You are not a member of THIS project 👥Ⓜ️",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );

        // 3. Check if the user is the leader or admin of the project or else throw a NotLeaderOfProjectException
        if (!projectFeignClient.isLeaderOrAdminOfProject(projectId, userId))
            throw new NotLeaderOfProjectException(
                    "👮🏻You are not the leader or admin of THIS project👮🏻",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );
    }


    /**
     * Builds a TaskResponse object.
     *
     * @param task the assigned task
     * @return a TaskResponse object with information about the newly created task and updated task.
     */
    public TaskResponse buildTaskResponse(Task task) {
        ProjectDTO project = projectFeignClient.getProjectAsDTO(task.getProjectId());

        Map<String, String> assignededUsersMap = new HashMap<>();
        task.getAssignedUsers().forEach(taskUser -> {
            UserDTO assignedUser = authFeignClient.getUserDTOById(taskUser.getTaskUserId());
            assignededUsersMap.put(assignedUser.getUserId().toString(), assignedUser.getUsername());
        });

        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .projectName(project.getProjectName())
                .taskStatus(task.getTaskStatus())
                .dueDate(task.getDueDate())
                .assignedUsers(assignededUsersMap)
                .build();
    }
//
//
//    /**
//     * Retrieves a set of users associated with a task and a project, and updates the statusResponseMap with the status of the assignment for each user.
//     *
//     * @param task              The task to assign.
//     * @param projectId         The project to which the task belongs.
//     * @param statusResponseMap The map to which the status of the assignment for each user will be added.
//     * @return A set of users associated with the task and the project.
//     */
//    public Set<UserDTO> getUsers(
//            Task task,
//            long projectId,
//            Map<String, String> statusResponseMap
//    ) {
//        return projectMembersRepository.findByProject(projectId)
//                .stream().map(projectMembers -> {
//                    User user = repositoryUtils.findUserById_OrElseThrow_ResourceNoFoundException(projectMembers.getUser().getUserId());
//                    // Check if the task is already assigned to the user, skip ahead and add a response to the map if it is
//                    if (taskRepository.findByAssignedUsersAndTaskId(user, task.getTaskId()).isPresent())
//                        statusResponseMap.put(user.getUsername(), "Fail: Task already assigned to user " + user.getUsername());
//                    else statusResponseMap.put(user.getUsername(), "Success");
//                    user.getTask().add(task);
//
//                    return userRepository.save(user);
//                }).collect(Collectors.toSet());
//    }

}
