package com.dev.vault.ProjectService.service.intercommunication;

import com.dev.vault.ProjectService.feign.client.AuthUserFeignClient;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.repository.ProjectRepository;
import com.dev.vault.ProjectService.util.ProjectUtilsImpl;
import com.dev.vault.ProjectService.util.RepositoryUtils;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectInterCommunicationService {

    private final ProjectRepository projectRepository;
    private final AuthUserFeignClient authUserFeignClient;
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


    public List<UserDTO> getUsersAssociatedWithTaskAndProject(long projectId) {
        return repositoryUtils.find_ProjectMembersByProjectId(projectId)
                .stream()
                .map(projectMembers -> authUserFeignClient.getUserDTOById(projectMembers.getUserId()))
                .toList();
    }


    public boolean validateProjectExists(long projectId) {
        return projectRepository.findById(projectId)
                .isPresent();
    }

}
