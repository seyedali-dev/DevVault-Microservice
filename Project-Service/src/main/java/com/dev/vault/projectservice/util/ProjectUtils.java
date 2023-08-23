//package com.dev.vault.projectservice.util;
//
//import com.dev.vault.projectservice.model.entity.Project;
//import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
//import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
//
///**
// * Utility interface for checking user-project relationships.
// */
//public interface ProjectUtils {
//
//    /**
//     * Checks if the given user is a leader or admin of the specified project.
//     *
//     * @param project The project to check.
//     * @param user    The user to check.
//     * @return True if the user is a leader or admin of the project, false otherwise.
//     */
//    boolean isLeaderOrAdminOfProject(Project project, User user);
//
//
//    /**
//     * Checks if the given user is a member of the specified project.
//     *
//     * @param project The project to check.
//     * @param user    The user to check.
//     * @return True if the user is a member of the project, false otherwise.
//     * @throws ResourceNotFoundException      If the project cannot be found.
//     * @throws ResourceAlreadyExistsException If the user has already sent a join project request for the project.
//     */
//    boolean isMemberOfProject(Project project, User user)
//            throws ResourceNotFoundException, ResourceAlreadyExistsException;
//
//}
//
