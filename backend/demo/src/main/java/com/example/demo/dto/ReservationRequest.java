package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "预约请求参数")
public class ReservationRequest {
    @Schema(description = "会议室ID", required = true)
    private Integer roomId;
    @Schema(description = "会议主题",  required = true)
    private String title;
    @Schema(description = "预约日期", required = true)
    private String date;
    @Schema(description = "开始时间", required = true)
    private String startTime;
    @Schema(description = "结束时间", required = true)
    private String endTime;
}
