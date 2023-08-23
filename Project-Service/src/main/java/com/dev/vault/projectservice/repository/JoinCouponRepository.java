package com.dev.vault.projectservice.repository;

import com.dev.vault.projectservice.model.entity.JoinCoupon;
import com.dev.vault.projectservice.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinCouponRepository extends JpaRepository<JoinCoupon, Long> {

    Optional<JoinCoupon> findByProjectAndRequestingUserAndCoupon(Project project, Long requestingUser, String coupon);


    Optional<JoinCoupon> findByCoupon(String coupon);


    Optional<JoinCoupon> findByRequestingUserAndProject(Long requestingUser, Project project);

}