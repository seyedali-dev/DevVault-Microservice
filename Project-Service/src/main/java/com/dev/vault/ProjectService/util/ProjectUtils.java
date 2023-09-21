package com.dev.vault.ProjectService.util;

import com.dev.vault.ProjectService.model.dto.UserMembersDto;
import com.dev.vault.ProjectService.model.entity.Project;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.UserDTO;

import java.util.List;

/**
 * Utility interface for checking user-project relationships.
 */
public interface ProjectUtils {

    /**
     * Checks if the given user is a leader or admin of the specified project.
     *
     * @param project The project to check.
     * @param userDTO The user to check.
     * @return True if the user is a leader or admin of the project, false otherwise.
     */
    boolean isLeaderOrAdminOfProject(Project project, UserDTO userDTO);


    /**
     * Checks if the given user is a member of the specified project.
     *
     * @param project The project to check.
     * @param userDTO The user to check.
     * @return True if the user is a member of the project, false otherwise.
     * @throws ResourceNotFoundException      If the project cannot be found.
     * @throws ResourceAlreadyExistsException If the user has already sent a join project request for the project.
     */
    boolean isMemberOfProject(Project project, UserDTO userDTO)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;


    /**
     * Returns a list of UserDto objects for a given project.
     *
     * @param project The project to get the list of members for.
     * @return A list of UserDto objects representing the members of the project.
     */
    List<UserMembersDto> getUserDtoList(Project project);

}

