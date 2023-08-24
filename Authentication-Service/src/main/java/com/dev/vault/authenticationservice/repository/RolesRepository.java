package com.dev.vault.authenticationservice.repository;

import com.dev.vault.authenticationservice.model.entity.Roles;
import com.dev.vault.shared.lib.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRole(Role role);

}