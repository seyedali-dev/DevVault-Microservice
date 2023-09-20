package com.dev.vault.ProjectService.service.intercommunication;

import com.dev.vault.ProjectService.feign.client.AuthUserFeignClient;
import com.dev.vault.ProjectService.feign.client.TaskFeignClient;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.util.ProjectUtilsImpl;
import com.dev.vault.ProjectService.util.RepositoryUtils;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectInterCommunicationService {

    private final AuthUserFeignClient authUserFeignClient;
    private final TaskFeignClient taskFeignClient;
    private final ProjectUtilsImpl projectUtils;
    private final RepositoryUtils repositoryUtils;

    public boolean isMemberOfProject(Long projectId, long userId) {
        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);
        UserDTO userDTO = authUserFeignClient.getUserDTOById(userId);

        return projectUtils.isMemberOfProject(project, userDTO);
    }


    public boolean isLeaderOrAdminOfProject(long projectId, long userId) {
        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);
        UserDTO userDTO = authUserFeignClient.getUserDTOById(userId);

        return projectUtils.isLeaderOrAdminOfProject(project, userDTO);
    }


    public ProjectDTO getProjectDTO(long projectId) {
        Project project = repositoryUtils.findProjectById_OrElseThrow_ResourceNotFoundException(projectId);
        return ProjectDTO.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .creationTime(project.getCreationTime())
                .leaderId(project.getLeaderId())
                .memberCount(project.getMemberCount())
                .build();
    }


    public List<UserDTO> getUsersAssociatedWithTaskAndProject(long taskId, long projectId) {
        // 1. Find the members of the project
        return repositoryUtils.find_ProjectMembersByProjectId(projectId)
                .stream().map(projectMembers -> {

                    UserDTO userDTO = authUserFeignClient.getUserDTOById(projectMembers.getUserId());

                    // 2. Check if the task is already assigned to the user; skip ahead, and add a response to the map
                    String alreadyAssignedMessage = "❌😖 Fail: Task is already assigned to user '" + userDTO.getUsername() + "' 😖❌";
                    String successMessage = "✅ Success: Task assigned to user '" + userDTO.getUsername() + "' ✅";
                    Map<String, String> statusResponseMap = new HashMap<>();

                    if (taskFeignClient.findTaskByAssignedUser_IsUserPresent(userDTO.getUserId(), taskId))
                        statusResponseMap.put(userDTO.getUsername(), alreadyAssignedMessage);
                    else statusResponseMap.put(userDTO.getUsername(), successMessage);

                    userDTO.setAssignedTaskIDs(List.of(taskId));
                    return authUserFeignClient.saveUserAndReturnSavedUserAsDTO(userDTO);

                }).toList();
    }

}
