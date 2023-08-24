package com.dev.vault.authenticationservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo controller for testing authorization and authentication. (since i'm not very good at writing tests)
 */
@SuppressWarnings("SameReturnValue")
@RestController
@RequestMapping("/authenticated")
public class DemoController {

    @GetMapping("/pl")
    @PreAuthorize("hasRole('PROJECT_LEADER')")
    public String demo() {
        return "PROJECT_LEADER :: this is for testing authentication and authorization";
    }


    @GetMapping("/pa")
    @PreAuthorize("hasRole('PROJECT_ADMIN')")
    public String demo1() {
        return "PROJECT_ADMIN :: this is for testing authentication and authorization";
    }


    @GetMapping("/tm")
    @PreAuthorize("hasRole('TEAM_MEMBER')")
    public String demo2() {
        return "TEAM_MEMBER :: this is for testing authentication and authorization";
    }


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('PROJECT_LEADER', 'PROJECT_ADMIN', 'TEAM_MEMBER')")
    public String demo3() {
        return "'PROJECT_LEADER', 'PROJECT_ADMIN', 'TEAM_MEMBER' :: this is for testing authentication and authorization";
    }

}
