//package com.dev.vault.projectservice.controller;
//
//import com.dev.vault.projectservice.service.interfaces.JoinCouponService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import static org.springframework.http.HttpStatus.CREATED;
//
///**
// * REST controller for generating one-time-use join coupon.
// */
//@RestController
//@RequestMapping("/api/v1/join_coupon")
//@RequiredArgsConstructor
//public class JoinCouponController {
//
//    private final JoinCouponService joinCouponService;
//
//    /**
//     * Generates a random UUID Join Project Request Coupon for a specific project.
//     *
//     * @param projectId        the ID of the coupon needs to be generated for.
//     * @param requestingUserId the ID of the user that wants to join a project.
//     * @return ResponseEntity containing the JoinRequestCoupon String from service.
//     */
//    @PostMapping("/{projectId}/{requestingUserId}")
//    public ResponseEntity<String> generateOneTimeJoinCoupon(@PathVariable Long projectId, @PathVariable Long requestingUserId) {
//        return new ResponseEntity<>("Your JoinCoupon: " + joinCouponService.generateOneTimeJoinCoupon(projectId, requestingUserId), CREATED);
//    }
//
//}
