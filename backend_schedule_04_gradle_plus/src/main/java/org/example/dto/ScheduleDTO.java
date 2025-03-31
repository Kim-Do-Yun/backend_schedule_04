package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDTO {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer categoryId;  // 카테고리 ID

    // 생성자
    public ScheduleDTO(String title, String description, LocalDateTime startTime, LocalDateTime endTime, Integer categoryId) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.categoryId = categoryId;
    }
}