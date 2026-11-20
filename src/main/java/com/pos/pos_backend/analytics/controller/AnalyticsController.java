package com.pos.pos_backend.analytics.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.pos_backend.analytics.dto.AnalyticsSummaryResponse;
import com.pos.pos_backend.analytics.dto.BestSellerResponse;
import com.pos.pos_backend.analytics.dto.DailySalesResponse;
import com.pos.pos_backend.analytics.service.AnalyticsService;
import com.pos.pos_backend.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/summary")
    public AnalyticsSummaryResponse summary(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return service.getSummary(
                principal.getBusinessId()
        );
    }

    /*@GetMapping("/daily-sales")
    public List<DailySalesResponse> dailySales(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return service.dailySales(
                principal.getBusinessId()
        );
    }

    @GetMapping("/best-sellers")
    public List<BestSellerResponse> bestSellers(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return service.bestSellers(
                principal.getBusinessId()
        );
    }*/
}