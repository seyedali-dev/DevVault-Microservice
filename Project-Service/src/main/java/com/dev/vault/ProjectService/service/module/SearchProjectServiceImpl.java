package com.dev.vault.ProjectService.service.module;

import com.dev.vault.ProjectService.feign.client.AuthUserFeignClient;
import com.dev.vault.ProjectService.feign.client.CommentFeignClient;
import com.dev.vault.ProjectService.model.dto.ProjectMembersDto;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.ProjectService.model.response.SearchResponse;
import com.dev.vault.ProjectService.repository.ProjectRepository;
import com.dev.vault.ProjectService.service.interfaces.SearchProjectService;
import com.dev.vault.ProjectService.util.ProjectUtilsImpl;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.CommentDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Service implementation for searching projects.
 */
@Slf4j
@Service
@RequiredArgsConstructor // TODO:: pagination
public class SearchProjectServiceImpl implements SearchProjectService {

    private final ProjectRepository projectRepository;
    private final AuthUserFeignClient authUserFeignClient;
    private final CommentFeignClient commentFeignClient;
    private final ProjectUtilsImpl projectUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SearchResponse> listAllProjects() {
        // 1. Get all projects from the project repository.
        List<Project> projects = projectRepository.findAll();

        // 2. Get all the comments of each of the projects.
        List<CommentDTO> commentDTOs = getCommentDTOs(projects);

        // 3. Map each project to a SearchResponse object and collect them into a list.
        return projects.stream().map(project -> {
                    try {
                        UserDTO leaderUserDTO = authUserFeignClient.getUserDTOById(project.getLeaderId());

                        return SearchResponse.builder()
                                .projectId(project.getProjectId())
                                .projectName(project.getProjectName())
                                .projectDescription(project.getDescription())
                                .leaderName(leaderUserDTO.getUsername())
                                .members(new ProjectMembersDto(projectUtils.getUserDtoList(project)))
                                .commentDTOs(commentDTOs)
                                .build();

                    } catch (Exception e) {
                        log.error("😖 uh oh... there seems to be an error: {{}} 😖", e.getMessage());
                        throw new DevVaultException(
                                e.getMessage(),
                                BAD_REQUEST,
                                BAD_REQUEST.value()
                        );
                    }
                }
        ).toList();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<SearchResponse> searchForProject(String projectName) {
        // 1. Search for projects with names containing the given string.
        List<Project> projects = projectRepository.findByProjectNameContaining(projectName);

        // 2. Throw an exception if no project is found.
        if (projects == null || projects.isEmpty())
            throw new ResourceNotFoundException(
                    "😖 huh... it seems the project with name '" + projectName + "' wasn't found in the db 😖",
                    NOT_FOUND,
                    NOT_FOUND.value()
            );

        // 3. Find the comments for each of the projects.
        List<CommentDTO> commentDTOs = getCommentDTOs(projects);

        // 4. Map each project to a SearchResponse object and collect them into a list.
        return projects.stream()
                .map(project -> {
                            String leaderName = authUserFeignClient.getUserDTOById(project.getLeaderId()).getUsername();

                            return SearchResponse.builder()
                                    .projectId(project.getProjectId())
                                    .projectName(project.getProjectName())
                                    .projectDescription(project.getDescription())
                                    .leaderName(leaderName)
                                    .members(new ProjectMembersDto(projectUtils.getUserDtoList(project)))
                                    .commentDTOs(commentDTOs)
                                    .build();
                        }
                ).toList();
    }


    private List<CommentDTO> getCommentDTOs(List<Project> projects) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Project project : projects)
            commentDTOs = commentFeignClient.getCommentListAsDTOs_ByProjectId(project.getProjectId());
        return commentDTOs;
    }

}
