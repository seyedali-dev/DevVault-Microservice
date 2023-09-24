package com.dev.vault.TaskService.service.module;

import com.dev.vault.TaskService.fegin.client.AuthUserFeignClient;
import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.entity.TaskUser;
import com.dev.vault.TaskService.model.request.TaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.TaskService.repository.TaskUserRepository;
import com.dev.vault.TaskService.service.interfaces.TaskManagementService;
import com.dev.vault.TaskService.util.RepositoryUtils;
import com.dev.vault.TaskService.util.TaskUtils;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.model.enums.TaskPriority;
import com.dev.vault.shared.lib.model.enums.TaskStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dev.vault.shared.lib.model.enums.TaskStatus.IN_PROGRESS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;


/**
 * Service implementation for task management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskUserRepository taskUserRepository;
    private final TaskRepository taskRepository;
    private final RepositoryUtils repositoryUtils;
    private final TaskUtils taskUtils;
    private final AuthUserFeignClient authUserFeignClient;
    private final ProjectUtilFeignClient projectUtilFeignClient;
    private final ModelMapper mapper;
    private final HttpServletRequest httpServletRequest;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TaskResponse createNewTask(Long projectId, TaskRequest taskRequest) {
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        validateUniqueName_Membership_Leadership_OfTask(projectId, taskRequest, currentUserDTO_Id);

        Task task = buildAndSaveTask(projectId, taskRequest, currentUserDTO_Id);

        return taskUtils.buildTaskResponse(task);
    }

    private void validateUniqueName_Membership_Leadership_OfTask(Long projectId, TaskRequest taskRequest, long currentUserId) {
        // Check if the currentUser is a member of the project
        if (!projectUtilFeignClient.isMemberOfProject(projectId, currentUserId))
            throw new NotMemberOfProjectException(
                    "Ⓜ️👥 You are not a member of THIS project 👥Ⓜ️",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );

        // Check if the currentUser is the leader or admin of the project
        if (!projectUtilFeignClient.isLeaderOrAdminOfProject(projectId, currentUserId))
            throw new NotLeaderOfProjectException(
                    "👮🏻You are not the Leader or Admin of THIS project👮🏻",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );

        // Check if a task with the same name already exists in the project
        if (taskUtils.doesTaskAlreadyExists(taskRequest, projectId))
            throw new ResourceAlreadyExistsException(
                    "⭕ Task '" + taskRequest.getTaskName() + "' already exists in db.. provide a unique name ⭕",
                    BAD_REQUEST,
                    BAD_REQUEST.value()
            );
    }

    private Task buildAndSaveTask(Long projectId, TaskRequest taskRequest, long currentUserId) {
        Task task = mapper.map(taskRequest, Task.class);
        task.setCreatedByUserId(currentUserId);
        task.setProjectId(projectId);
        task.setCreatedAt(LocalDateTime.now());
        task.setTaskStatus(IN_PROGRESS);

        TaskUser assignedUsers = buildAndSaveTaskUser(currentUserId, task);
        task.getAssignedUsers().add(assignedUsers);

        return taskRepository.save(task);
    }

    private TaskUser buildAndSaveTaskUser(long currentUserId, Task task) {
        TaskUser taskUser = TaskUser.builder()
                .userId(currentUserId)
                .task(task)
                .build();
        return taskUserRepository.save(taskUser);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {
        Task foundTask = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        updateTaskDetails(foundTask, taskRequest);
        return taskUtils.buildTaskResponse(foundTask);
    }

    private void updateTaskDetails(Task foundTask, TaskRequest taskRequest) {
        if (taskRequest.getTaskName() != null) foundTask.setTaskName(taskRequest.getTaskName());

        if (taskRequest.getTaskPriority() != null) foundTask.setTaskPriority(taskRequest.getTaskPriority());

        if (taskRequest.getTaskStatus() != null) foundTask.setTaskStatus(taskRequest.getTaskStatus());

        if (taskRequest.getDescription() != null) foundTask.setDescription(taskRequest.getDescription());

        if (taskRequest.getDueDate() != null) foundTask.setDueDate(taskRequest.getDueDate());

        taskRepository.save(foundTask);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResponse getTaskDetails(Long taskId) {
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        return taskUtils.buildTaskResponse(task);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(long projectId, long taskId) {
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUserDTOs_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);

        if (!projectUtilFeignClient.isLeaderOrAdminOfProject(projectId, currentUserDTOs_Id))
            throw new NotLeaderOfProjectException(
                    "👮🏻 You are not a leader/admin of THIS project! 👮🏻",
                    FORBIDDEN,
                    FORBIDDEN.value()
            );

        taskRepository.deleteById(taskId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskResponse> searchTaskBasedOnDifferentCriteria(TaskStatus status, TaskPriority priority, Long projectId, Long assignedTo_UserId) {
        // Using Set since I don't want any duplicate searches (tasks) to get added to the list!
        Set<Task> taskList = new HashSet<>();
        List<TaskResponse> taskResponses = new ArrayList<>();

        if (status != null)
            taskList.addAll(taskRepository.findByTaskStatus(status));

        if (priority != null)
            taskList.addAll(taskRepository.findByTaskPriority(priority));

        if (projectId != null)
            taskList.addAll(taskRepository.findByProjectId(projectId));

        if (assignedTo_UserId != null) {
            taskList.addAll(
                    // a) first finds the taskUser since the task and user are mapped together using this class.
                    taskUserRepository.findTaskUserByUserId(assignedTo_UserId)
                            .stream() // b) then stream over it and grab out the task!
                            .map(TaskUser::getTask)
                            .toList()
            );
        }

        taskList.forEach(
                task -> taskResponses.add(taskUtils.buildTaskResponse(task))
        );

        return taskResponses;
    }

}
