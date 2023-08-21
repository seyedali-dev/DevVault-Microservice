package com.dev.vault.ProjectService.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity for generating a JoinRequestCoupon for user's that want to make join request to a project.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "join_coupon")
public class JoinCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;
    private String coupon;

    /* relationships */
    private Long requestingUser;

    private Long leaderId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    /* end of relationships */

    private boolean used = false;

    public JoinCoupon(Long requestingUserId, Long leaderId, Project project, String coupon) {
        this.requestingUser = requestingUserId;
        this.leaderId = leaderId;
        this.project = project;
        this.coupon = coupon;
    }

}
