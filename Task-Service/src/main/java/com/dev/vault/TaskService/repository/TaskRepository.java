package com.dev.vault.TaskService.repository;

import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.enums.TaskPriority;
import com.dev.vault.TaskService.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByProjectIdAndTaskName(Long projectId, String taskName);

    Optional<Task> findByProjectId(Long projectId);


    Optional<Task> findByTaskPriority(TaskPriority taskPriority);


    Optional<Task> findByTaskStatus(TaskStatus taskStatus);

}