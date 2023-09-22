package com.dev.vault.AuthenticationService.repository;

import com.dev.vault.AuthenticationService.model.entity.Roles;
import com.dev.vault.shared.lib.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRole(Role role);

}