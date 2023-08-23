//package com.dev.vault.projectservice.util;
//
//import com.dev.vault.projectservice.model.entity.Project;
//import com.dev.vault.projectservice.model.entity.ProjectMembers;
//import com.dev.vault.projectservice.model.entity.UserProjectRole;
//import com.dev.vault.projectservice.repository.ProjectMembersRepository;
//import com.dev.vault.projectservice.repository.UserProjectRoleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
///**
// * Primary implementation of ProjectUtils for regular project membership checks.
// */
//@Primary
//@Service
//@RequiredArgsConstructor
//public class ProjectUtilsImpl implements ProjectUtils {
//
//    private final UserProjectRoleRepository userProjectRoleRepository;
//    private final ProjectMembersRepository projectMembersRepository;
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean isLeaderOrAdminOfProject(Project project, User user) {
//        // Find the user's role in the project
//        Roles leaderOrAdminRole = user.getRoles().stream()
//                .filter(roles ->
//                        roles.getRole().equals(Role.PROJECT_LEADER) ||
//                        roles.getRole().equals(Role.PROJECT_ADMIN)
//                ).findFirst()
//                .orElse(null);
//
//        // Find the user's role for the specified project
//        assert leaderOrAdminRole != null;
//        Optional<UserProjectRole> userProjectRole
//                = userProjectRoleRepository.findByUserIdAndRoleIdAndProject(user.getUserId(), leaderOrAdminRole.getRoleId(), project);
//
//        // Return true if the user has the leader or admin role in the project, false otherwise
//        return userProjectRole.isPresent() &&
//               userProjectRole.get().getRoleId().equals(leaderOrAdminRole.getRoleId());
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean isMemberOfProject(Project project, User user) {
//        Optional<ProjectMembers> members = projectMembersRepository.findByProject_ProjectNameAndUserId(project.getProjectName(), user.getUserId());
//        return members.isPresent();
//    }
//
//}
