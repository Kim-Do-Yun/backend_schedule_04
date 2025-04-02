package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    private int minutesBefore; // n분 전 설정 (예: 30분 전)

    // ✅ (Schedule, int) 생성자가 없으면 추가하자!
    public Reminder(Schedule schedule, int minutesBefore) {
        this.schedule = schedule;
        this.minutesBefore = minutesBefore;
    }

}

