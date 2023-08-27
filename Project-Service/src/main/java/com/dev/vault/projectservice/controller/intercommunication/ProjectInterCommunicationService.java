package com.dev.vault.projectservice.controller.intercommunication;

import com.dev.vault.projectservice.feign.client.AuthUserFeignClient;
import com.dev.vault.projectservice.model.entity.Project;
import com.dev.vault.projectservice.util.ProjectUtilsImpl;
import com.dev.vault.projectservice.util.RepositoryUtils;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectInterCommunicationService {

    private final ProjectUtilsImpl projectUtils;
    private final RepositoryUtils repositoryUtils;
    private final AuthUserFeignClient authUserFeignClient;

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

}
