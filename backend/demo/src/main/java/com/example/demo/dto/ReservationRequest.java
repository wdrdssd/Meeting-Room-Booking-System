package com.example.demo.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private Integer roomId;
    private String title;
    private String date;
    private String startTime;
    private String endTime;
}
