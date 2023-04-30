package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.response.statistics.StatisticsResponse;
import com.ktu.xsports.api.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.ktu.xsports.api.util.ApiVersionPrefix.API_V1;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + "/statistics")
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping()
    public ResponseEntity<?> getStatistics(
        @AuthenticationPrincipal User user
    ) {
        StatisticsResponse response = statisticsService.getStatistics(user.getId());
        return ResponseEntity.ok(Map.of("data", response));
    }
}
