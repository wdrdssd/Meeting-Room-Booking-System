package com.example.demo.service;

import com.example.demo.dao.MeetingRoomDao;
import com.example.demo.dao.ReservationDao;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private MeetingRoomDao meetingRoomDao;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private UserService userService;


    public boolean checkConfict(Integer roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> reservations = reservationDao.selectByRoomAndDate(roomId, date);
        for (Reservation r : reservations) {
            boolean isOverlap = startTime.isBefore(r.getEndTime()) && endTime.isAfter(r.getStartTime());
            if (isOverlap) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Reservation createReservation(Long userId, ReservationRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime startTime = LocalTime.parse(request.getStartTime());
        LocalTime endTime = LocalTime.parse(request.getEndTime());

        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new RuntimeException("开始时间必须早于结束时间");
        }
        if (meetingRoomService.getMeetingRoomById(request.getRoomId()) == null) {
            throw new RuntimeException("会议室不存在");
        }
        if (checkConfict(request.getRoomId(), date, startTime, endTime)) {
            throw new RuntimeException("该时段已被预约，请选择其他时间");
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setRoomId(request.getRoomId());
        reservation.setReserveDate(date);
        reservation.setTitle(request.getTitle());

        reservationDao.insert(reservation);
        return reservation;
    }


    public List<Reservation> getReservations(Long userId) {
        List<Reservation> reservations = reservationDao.selectByUserId(userId);
        for (Reservation r : reservations) {
            if (r.getUserId() != null) {
                User user = userService.getUserById(r.getUserId());
                if (user != null) {
                    r.setUserName(user.getName());
                } else {
                    r.setUserName("未知用户");
                }
            }
        }
        return reservations;
    }

    @Transactional
    public boolean deleteReservation(Integer reservationId, Long userId) {
        int rows = reservationDao.updateStatus(reservationId, userId);
        return rows > 0;
    }


    public List<Reservation> getReservationsByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Reservation> reservations = reservationDao.selectByDate(startDate, endDate);

        for (Reservation r : reservations) {
            if (r.getUserId() != null) {
                User user = userService.getUserById(r.getUserId());
                if (user != null) {
                    r.setUserName(user.getName());
                } else {
                    r.setUserName("未知用户");
                }
            }
        }

        return reservations;
    }

    @Transactional
    public int updateOngoingStatus() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        return reservationDao.updateStatusToOngoing(today, now);
    }

    @Transactional
    public int updateCompletedStatus() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        return reservationDao.updateStatusToCompleted(today, now);
    }
}