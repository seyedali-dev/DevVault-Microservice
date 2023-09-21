package com.dev.vault.ProjectService.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String projectName;
    private String description;
    @CreationTimestamp
    private LocalDate createdAt;
    @CreationTimestamp
    private LocalTime creationTime;
    private int memberCount;

    /* relationships */
    private Long leaderId;
    /* end of relationships */

    public void incrementMemberCount() {
        this.memberCount++;
    }

}
