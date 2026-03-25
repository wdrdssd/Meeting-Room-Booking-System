package com.example.demo.service;

import com.example.demo.dao.MeetingRoomDao;
import com.example.demo.dao.ReservationDao;
import com.example.demo.entity.MeetingRoom;
import com.example.demo.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private MeetingRoomDao meetingRoomDao;

    /**
     * 获取统计概览
     */
    public Map<String, Long> getOverview() {
        List<Reservation> allReservations = reservationDao.selectAll();

        long total = allReservations.size();
        long ongoing = allReservations.stream().filter(r -> "2".equals(r.getStatus())).count();
        long completed = allReservations.stream().filter(r -> "3".equals(r.getStatus())).count();
        long pending = allReservations.stream().filter(r -> "4".equals(r.getStatus())).count();

        Map<String, Long> result = new HashMap<>();
        result.put("total", total);
        result.put("ongoing", ongoing);
        result.put("completed", completed);
        result.put("pending", pending);

        return result;
    }

    /**
     * 获取近7天预约趋势
     */
    public Map<String, Object> getTrend() {
        // 计算近7天的日期
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            dates.add(date.toString());

            // 查询当天的预约数量
            List<Reservation> reservations = reservationDao.selectByDate(date, date);
            counts.add(reservations.size());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("counts", counts);

        return result;
    }

    /**
     * 获取会议室使用排行
     */
    public Map<String, Object> getRoomRank() {
        // 获取所有会议室
        List<MeetingRoom> rooms = meetingRoomDao.findAllMeetingRoom();

        // 获取所有已预约的预约（状态1、2、3）
        List<Reservation> reservations = reservationDao.selectAll()
                .stream()
                .filter(r -> "1".equals(r.getStatus()) || "2".equals(r.getStatus()) || "3".equals(r.getStatus()))
                .collect(Collectors.toList());

        // 统计每个会议室的预约次数
        Map<Integer, Integer> countMap = new HashMap<>();
        for (Reservation r : reservations) {
            countMap.put(r.getRoomId(), countMap.getOrDefault(r.getRoomId(), 0) + 1);
        }

        // 构建排行列表
        List<Map<String, Object>> rankList = new ArrayList<>();
        for (MeetingRoom room : rooms) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", room.getRoomName());
            item.put("count", countMap.getOrDefault(room.getId(), 0));
            rankList.add(item);
        }

        // 按预约次数降序排序
        rankList.sort((a, b) -> (Integer) b.get("count") - (Integer) a.get("count"));

        // 提取数据
        List<String> names = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (Map<String, Object> item : rankList) {
            names.add((String) item.get("name"));
            counts.add((Integer) item.get("count"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("names", names);
        result.put("counts", counts);

        return result;
    }
}