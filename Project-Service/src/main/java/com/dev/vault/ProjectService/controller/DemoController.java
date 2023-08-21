package com.dev.vault.ProjectService.controller;

import com.dev.vault.shared.lib.exceptions.DevVaultException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Demo controller for testing authorization and authentication. (since i'm not very good at writing tests)
 */
@RestController
@RequestMapping("/project-demo/authenticated")
public class DemoController {

    @GetMapping("/pl")
//    @PreAuthorize("hasRole('PROJECT_LEADER')")
    public String demo() {
        return "PROJECT_LEADER :: this is for testing authentication and authorization";
    }


    @GetMapping("/pa")
//    @PreAuthorize("hasRole('PROJECT_ADMIN')")
    public String demo1() {
        throw new DevVaultException("demo exception", BAD_REQUEST, BAD_REQUEST.value());
    }


    @GetMapping("/tm")
//    @PreAuthorize("hasRole('TEAM_MEMBER')")
    public String demo2() {
        return "TEAM_MEMBER :: this is for testing authentication and authorization";
    }


    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('PROJECT_LEADER', 'PROJECT_ADMIN', 'TEAM_MEMBER')")
    public String demo3() {
        return "'PROJECT_LEADER', 'PROJECT_ADMIN', 'TEAM_MEMBER' :: this is for testing authentication and authorization";
    }

}
