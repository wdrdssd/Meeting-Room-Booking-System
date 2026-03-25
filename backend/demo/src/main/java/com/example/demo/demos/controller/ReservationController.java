package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.entity.Reservation;
import com.example.demo.service.ReservationService;
import com.example.demo.tools.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "预约管理", description = "预约相关的所有接口")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null &&token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwt.getUserId(token);
    }

    @PostMapping
    @Operation(summary = "创建预约", description = "用户创建新的会议室预约")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "预约成功"),
            @ApiResponse(responseCode = "400", description = "参数错误或时间冲突")
    })
    public Result<Reservation> cereat(@RequestBody ReservationRequest  request,HttpServletRequest httpServletRequest ) {
        try {
            Long userId = getUserId(httpServletRequest);
            Reservation reservation = reservationService.createReservation(userId,request);
            return Result.success(reservation);
        }catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    @Operation(summary = "获取我的预约", description = "获取当前登录用户的所有预约记录")
    public Result<List<Reservation>> getMyReservations(HttpServletRequest request ) {
        Long userId = getUserId(request);
        List<Reservation> reservations = reservationService.getReservations(userId);
        return Result.success(reservations);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "取消指定的预约，只能取消自己的预约")
    public Result<Void> cancel(@Parameter(description = "预约ID") @PathVariable Integer id,HttpServletRequest request) {
        Long userId = getUserId(request);
        boolean success = reservationService.deleteReservation(id,userId);
        if (success) {
            return Result.success(null);
        }else {
            return Result.error("取消失败，预约不存在或无权操作");
        }
    }

    @PostMapping("/check")
    @Operation(summary = "检查冲突", description = "检查指定时间段是否已被预约")
    public Result<Boolean> check(@RequestBody ReservationRequest request) {
        try {
            java.time.LocalDate date = java.time.LocalDate.parse(request.getDate());
            java.time.LocalTime startTime = java.time.LocalTime.parse(request.getStartTime());
            java.time.LocalTime endTime = java.time.LocalTime.parse(request.getEndTime());
            boolean hasConflict = reservationService.checkConfict(request.getRoomId(), date, startTime, endTime);
            return Result.success(hasConflict);
        }catch (Exception e) {
            return Result.error("参数错误");
        }

    }

    @GetMapping("/calendar")
    @Operation(summary = "获取日历数据", description = "获取指定月份的所有预约，用于日历展示")
    public Result<List<Reservation>> getCalendar(@Parameter(description = "年份") @RequestParam int year,@Parameter(description = "月份") @RequestParam int month ) {
        List<Reservation> reservations = reservationService.getReservationsByMonth(year,month);
        return Result.success(reservations);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有预约（管理员）")
    public Result<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return Result.success(reservations);
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "审批预约（管理员）")
    public Result<Void> approve(
            @Parameter(description = "预约ID") @PathVariable Integer id,
            @Parameter(description = "是否通过") @RequestParam boolean approve) {

        try {
            reservationService.approveReservation(id, approve);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}

