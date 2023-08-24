package com.dev.vault.projectservice.service.module;

import com.dev.vault.projectservice.feign.client.AuthUserFeignClient;
import com.dev.vault.projectservice.model.dto.ProjectMembersDto;
import com.dev.vault.projectservice.model.dto.UserMembersDto;
import com.dev.vault.projectservice.model.entity.Project;
import com.dev.vault.projectservice.model.entity.ProjectMembers;
import com.dev.vault.projectservice.model.response.SearchResponse;
import com.dev.vault.projectservice.repository.ProjectMembersRepository;
import com.dev.vault.projectservice.repository.ProjectRepository;
import com.dev.vault.projectservice.service.interfaces.SearchProjectService;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
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
    private final ProjectMembersRepository projectMembersRepository;
    private final AuthUserFeignClient authUserFeignClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SearchResponse> listAllProjects() {
        // Get all projects from the project repository
        List<Project> projects = projectRepository.findAll();

        // Map each project to a SearchResponse object and collect them into a list
        return projects.stream().map(project -> {
                    try {
                        UserDTO leaderUserDTO = authUserFeignClient.getUserDTO(project.getLeaderId());

                        return SearchResponse.builder()
                                .projectId(project.getProjectId())
                                .projectName(project.getProjectName())
                                .projectDescription(project.getDescription())
                                .leaderName(leaderUserDTO.getUsername())
                                .members(new ProjectMembersDto(getUserDtoList(project)))
                                .build();

                    } catch (Exception e) {
                        log.error("😖 uh oh... there seems to be an error: {{}} 😖", e.getMessage());
                        throw new DevVaultException(e.getMessage(), BAD_REQUEST, BAD_REQUEST.value());
                    }
                }
        ).toList();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<SearchResponse> searchForProject(String projectName) {
        // Search for projects with names containing the given string
        List<Project> projects = projectRepository.findByProjectNameContaining(projectName);

        // Throw an exception if no project is found
        if (projects == null || projects.isEmpty())
            throw new ResourceNotFoundException(String.format("Project with given name: {%s} was not found", projectName), NOT_FOUND);

        // Map each project to a SearchResponse object and collect them into a list
        return projects.stream()
                .map(project -> {
                            String leaderName = authUserFeignClient.getUserNameById(project.getLeaderId());

                            return SearchResponse.builder()
                                    .projectId(project.getProjectId())
                                    .projectName(project.getProjectName())
                                    .projectDescription(project.getDescription())
                                    .leaderName(leaderName)
                                    .members(new ProjectMembersDto(getUserDtoList(project)))
                                    .build();
                        }
                ).toList();
    }


    /**
     * Returns a list of UserDto objects for a given project.
     *
     * @param project The project to get the list of members for.
     * @return A list of UserDto objects representing the members of the project.
     */
    private List<UserMembersDto> getUserDtoList(Project project) {
        // Get all project members associated with the given project
        List<ProjectMembers> members = projectMembersRepository.findByProject(project);

        // Create a list of UserDto objects for the project members
        ArrayList<UserMembersDto> userDtos = new ArrayList<>();
        for (ProjectMembers projectMembers : members) {
            UserDTO memberUserDTO = authUserFeignClient.getUserDTO(projectMembers.getUserId());

            UserMembersDto userDto = UserMembersDto.builder()
                    .username(memberUserDTO.getUsername())
                    .major(memberUserDTO.getMajor())
                    .education(memberUserDTO.getEducation())
                    .role(memberUserDTO.getRoles()
                            .stream().map(roles -> roles.getRole().name()).toList()
                    ).build();
            userDtos.add(userDto);
        }
        return userDtos;
    }

}
