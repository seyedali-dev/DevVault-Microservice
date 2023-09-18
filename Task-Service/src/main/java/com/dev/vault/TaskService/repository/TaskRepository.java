package com.dev.vault.TaskService.repository;

import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.entity.TaskUser;
import com.dev.vault.TaskService.model.enums.TaskPriority;
import com.dev.vault.TaskService.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
//    List<Task> findByAssignedUsersAndTaskId(List<TaskUser> assignedUsers, Long taskId);

    boolean existsByAssignedUsersTaskUserIdAndTaskId(Long taskUserId, Long taskId);

    Optional<Task> findByAssignedUsers_UserIdAndTaskId(long userId, Long taskId);


    Optional<Task> findByProjectIdAndTaskName(Long projectId, String taskName);


    Optional<Task> findByProjectId(Long projectId);


    Optional<Task> findByTaskPriority(TaskPriority taskPriority);


    Optional<Task> findByTaskStatus(TaskStatus taskStatus);

}