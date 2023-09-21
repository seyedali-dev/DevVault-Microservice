package com.dev.vault.TaskService.repository;

import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.model.entity.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskUserRepository extends JpaRepository<TaskUser, Long> {
    Optional<TaskUser> findByUserId(Long userId);


    List<TaskUser> findByTask(Task task);


    boolean existsByUserIdAndTask_TaskId(long userId, Long taskId);

}