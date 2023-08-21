package com.dev.vault.ProjectService.repository;

import com.dev.vault.ProjectService.model.dto.User;
import com.dev.vault.ProjectService.model.entity.JoinCoupon;
import com.dev.vault.ProjectService.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface JoinCouponRepository extends JpaRepository<JoinCoupon, Long> {

    Optional<JoinCoupon> findByProjectAndRequestingUserAndCoupon(Project project, Long requestingUser, String coupon);


    Optional<JoinCoupon> findByCoupon(String coupon);


    Optional<JoinCoupon> findByRequestingUserAndProject(Long requestingUser, Project project);

}