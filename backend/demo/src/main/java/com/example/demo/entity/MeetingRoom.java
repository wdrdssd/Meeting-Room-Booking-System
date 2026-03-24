package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "会议室实体")
public class MeetingRoom {
    @Schema(description = "会议室ID")
    private Integer id;
    @Schema(description = "会议室名称")
    private String roomName;
    @Schema(description = "状态：0-维护中，1-可用，2-禁用", allowableValues = {"0", "1", "2"})
    private String status;
}
