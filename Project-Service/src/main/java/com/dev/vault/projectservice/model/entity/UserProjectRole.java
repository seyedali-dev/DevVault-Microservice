package com.dev.vault.ProjectService.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity for managing the relationship of a PROJECT_LEADER with a specific project.
 * This class is used to check if a user is a project leader of a specific project, not other projects.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_project_role")
public class UserProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProjectRoleId;

    /* relationships */
    private Long userId;
    private Long roleId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    /* end of relationships */

    public UserProjectRole(Long userId, Long roleId, Project project) {
        this.userId = userId;
        this.roleId = roleId;
        this.project = project;
    }

}
