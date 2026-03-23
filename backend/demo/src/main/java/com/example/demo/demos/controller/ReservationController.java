package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.entity.Reservation;
import com.example.demo.service.ReservationService;
import com.example.demo.tools.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
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
    public Result<List<Reservation>> getMyReservations(HttpServletRequest request ) {
        Long userId = getUserId(request);
        List<Reservation> reservations = reservationService.getReservations(userId);
        return Result.success(reservations);
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Integer id,HttpServletRequest request) {
        Long userId = getUserId(request);
        boolean success = reservationService.deleteReservation(id,userId);
        if (success) {
            return Result.success(null);
        }else {
            return Result.error("取消失败，预约不存在或无权操作");
        }
    }

    @PostMapping("/check")
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
    public Result<List<Reservation>> getCalendar(@RequestParam int year,@RequestParam int month ) {
        List<Reservation> reservations = reservationService.getReservationsByMonth(year,month);
        return Result.success(reservations);
    }
}

