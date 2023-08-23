//package com.dev.vault.projectservice.service.module;
//
//import com.dev.vault.projectservice.model.dto.ProjectMembersDto;
//import com.dev.vault.projectservice.model.dto.UserMembersDto;
//import com.dev.vault.projectservice.model.entity.Project;
//import com.dev.vault.projectservice.model.entity.ProjectMembers;
//import com.dev.vault.projectservice.model.response.SearchResponse;
//import com.dev.vault.projectservice.repository.ProjectMembersRepository;
//import com.dev.vault.projectservice.repository.ProjectRepository;
//import com.dev.vault.projectservice.service.interfaces.SearchProjectService;
//import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.http.HttpStatus.NOT_FOUND;
//
///**
// * Service implementation for searching projects.
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor // TODO:: pagination
//public class SearchProjectServiceImpl implements SearchProjectService {
//
//    private final ProjectRepository projectRepository;
//    private final ProjectMembersRepository projectMembersRepository;
//    private final RetrieveDataService retrieveDataService;
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<SearchResponse> listAllProjects() {
//        // Get all projects from the project repository
//        List<Project> projects = projectRepository.findAll();
//
//        // Map each project to a SearchResponse object and collect them into a list
//        return projects.stream().map(project -> {
//                    User user = retrieveDataService.get_UserById_FromAuthenticationService(project.getLeaderId());
//
//                    return SearchResponse.builder()
//                            .projectId(project.getProjectId())
//                            .projectName(project.getProjectName())
//                            .projectDescription(project.getDescription())
//                            .leaderName(user.getUsername())
//                            .members(new ProjectMembersDto(getUserDtoList(project)))
//                            .build();
//                }
//        ).toList();
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<SearchResponse> searchForProject(String projectName) {
//        // Search for projects with names containing the given string
//        List<Project> project = projectRepository.findByProjectNameContaining(projectName);
//
//        // Throw an exception if no project is found
//        if (project == null || project.isEmpty())
//            throw new ResourceNotFoundException(String.format("Project with given name: {%s} was not found", projectName), NOT_FOUND);
//
//        // Map each project to a SearchResponse object and collect them into a list
//        return project.stream()
//                .map(projects -> {
//                            User user = retrieveDataService.get_UserById_FromAuthenticationService(projects.getLeaderId());
//
//                            return SearchResponse.builder()
//                                    .projectId(projects.getProjectId())
//                                    .projectName(projects.getProjectName())
//                                    .projectDescription(projects.getDescription())
//                                    .leaderName(user.getUsername())
//                                    .members(new ProjectMembersDto(getUserDtoList(projects)))
//                                    .build();
//                        }
//                ).toList();
//    }
//
//
//    /**
//     * Returns a list of UserDto objects for a given project.
//     *
//     * @param project The project to get the list of members for.
//     * @return A list of UserDto objects representing the members of the project.
//     */
//    private List<UserMembersDto> getUserDtoList(Project project) {
//        // Get all project members associated with the given project
//        List<ProjectMembers> members = projectMembersRepository.findByProject(project);
//
//        // Create a list of UserDto objects for the project members
//        ArrayList<UserMembersDto> userDtos = new ArrayList<>();
//        for (ProjectMembers projectMembers : members) {
//            User user = retrieveDataService.get_UserById_FromAuthenticationService(projectMembers.getUserId());
//            UserMembersDto userDto = UserMembersDto.builder()
//                    .username(user.getUsername())
//                    .major(user.getMajor())
//                    .education(user.getEducation())
//                    .role(user.getRoles()
//                            .stream().map(roles -> roles.getRole().name()).toList()
//                    )
//                    .build();
//            userDtos.add(userDto);
//        }
//        return userDtos;
//    }
//
//}
