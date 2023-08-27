//package com.dev.vault.TaskService.controller;
//
//import com.dev.vault.model.task.enums.TaskStatus;
//import com.dev.vault.service.interfaces.task.TaskProgressService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * REST controller for marking tasks as completed.
// * Requires the `PROJECT_LEADER` or `PROJECT_ADMIN` role to access.
// */
//@RestController
//@RequestMapping("/api/v1/task/progress")
//@RequiredArgsConstructor
//@PreAuthorize("hasAnyRole('PROJECT_LEADER','PROJECT_ADMIN')")
//public class TaskProgressController {
//
//    private final TaskProgressService taskService;
//
//    /**
//     * Marks a task as completed.
//     *
//     * @param taskId     the ID of the task to mark as completed (passed as a request parameter).
//     * @param projectId  the ID of the project that the task belongs to (passed as a request parameter).
//     * @param taskStatus the new status of the task (passed as a request parameter).
//     * @return a ResponseEntity with a status of 200 OK if the task was successfully marked as completed.
//     */
//    @PostMapping
//    public ResponseEntity<Void> markTaskAsCompleted(
//            @RequestParam("taskId") Long taskId,
//            @RequestParam("projectId") Long projectId,
//            @RequestParam TaskStatus taskStatus
//    ) {
//        taskService.markTaskAsCompleted(taskId, projectId, taskStatus);
//        return ResponseEntity.ok().build();
//    }
//}
