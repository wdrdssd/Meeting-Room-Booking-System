package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@Tag(name = "统计管理", description = "数据统计相关接口")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;


    @GetMapping("/overview")
    @Operation(summary = "获取统计概览")
    public Result<Map<String, Long>> getOverview() {
        Map<String, Long> overview = statisticsService.getOverview();
        return Result.success(overview);
    }


    @GetMapping("/trend")
    @Operation(summary = "获取近7天预约趋势")
    public Result<Map<String, Object>> getTrend() {
        Map<String, Object> trend = statisticsService.getTrend();
        return Result.success(trend);
    }


    @GetMapping("/rooms")
    @Operation(summary = "获取会议室使用排行")
    public Result<Map<String, Object>> getRoomRank() {
        Map<String, Object> rank = statisticsService.getRoomRank();
        return Result.success(rank);
    }
}