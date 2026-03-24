package com.example.demo.scheduler;

import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {
    @Autowired
    private ReservationService reservationService;

    @Scheduled(cron = "0 * * * * ?")
    public void updateReservationStatus() {
        try {
            // 更新进行中的预约
            int ongoingCount = reservationService.updateOngoingStatus();
            if (ongoingCount > 0) {
                System.out.println("更新了 " + ongoingCount + " 条预约为【进行中】");
            }

            // 更新已完成的预约
            int completedCount = reservationService.updateCompletedStatus();
            if (completedCount > 0) {
                System.out.println("更新了 " + completedCount + " 条预约为【已完成】");
            }

            if (ongoingCount == 0 && completedCount == 0) {
                System.out.println("没有需要更新的预约");
            }
        } catch (Exception e) {
            System.err.println("定时任务执行失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
