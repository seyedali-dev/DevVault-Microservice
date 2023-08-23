package com.dev.vault.projectservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {

    private String projectName;
    private String projectDescription;
    private LocalDate createdAt;
    private LocalTime creationTime;

}
