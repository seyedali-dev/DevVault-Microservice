package com.dev.vault.TaskService.util;

import com.dev.vault.TaskService.fegin.client.ProjectUtilFeignClient;
import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.repository.TaskRepository;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepositoryUtils {

    private final TaskRepository taskRepository;
    private final ProjectUtilFeignClient projectUtilFeignClient;

    public Task find_TaskById_OrElseThrow_ResourceNotFoundException(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems the task with ID {{}} wasn't found in the db 😖", taskId);
                    return new ResourceNotFoundException(
                            "😖 huh... it seems the project with  ID {" + taskId + "} wasn't found in the db 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }


    public ProjectDTO find_ProjectDTOById_OrElseThrow_ResourceNotFoundException(Long projectId) {
        return projectUtilFeignClient.getProjectAsDTO(projectId);
    }

}
