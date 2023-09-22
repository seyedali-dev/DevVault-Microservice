package com.dev.vault.TaskService.service.intercommunication;

import com.dev.vault.TaskService.model.entity.Task;
import com.dev.vault.TaskService.util.RepositoryUtils;
import com.dev.vault.TaskService.util.TaskUtils;
import com.dev.vault.shared.lib.model.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskInterCommunicationService {

    private final RepositoryUtils repositoryUtils;
    private final TaskUtils taskUtils;
    private final ModelMapper modelMapper;

    public TaskDTO findTaskById(long taskId) {
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        return modelMapper.map(
                task,
                TaskDTO.class
        );
    }


    public void validateTaskAndProjectAndUser(long taskId, long projectId, long userId) {
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        taskUtils.validateTaskAndProject(task, projectId, userId);
    }

}
