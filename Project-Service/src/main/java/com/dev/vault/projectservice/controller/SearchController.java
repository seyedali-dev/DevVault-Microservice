package com.dev.vault.projectservice.controller;

import com.dev.vault.projectservice.model.response.SearchResponse;
import com.dev.vault.projectservice.service.interfaces.SearchProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for searching projects.
 */
@RestController
@RequestMapping("/api/v1/search_project")
@RequiredArgsConstructor
public class SearchController {

    private final SearchProjectService searchProjectService;

    /**
     * Returns a list of all projects.
     *
     * @return a ResponseEntity containing a list of SearchResponse objects
     */
    @GetMapping
    public ResponseEntity<List<SearchResponse>> searchResultForAllProjects() {
        return ResponseEntity.ok(searchProjectService.listAllProjects());
    }


    /**
     * Returns a list of projects that match the provided projectOrGroupName.
     *
     * @param projectOrGroupName the name of the project to search for
     * @return a ResponseEntity containing a list of SearchResponse objects
     */
    @GetMapping("/{projectName}")
    public ResponseEntity<List<SearchResponse>> searchForAProjectOrGroup(@PathVariable String projectName) {
        return ResponseEntity.ok(searchProjectService.searchForProject(projectName));
    }

}
