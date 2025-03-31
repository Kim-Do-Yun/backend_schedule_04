package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.dto.ScheduleDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId; // 일정 ID

    private String firebaseUid; // 사용자 고유 ID
    private String title; // 일정 제목
    private String description; // 일정 설명
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간

    private Integer categoryId; // 카테고리 ID

    private Boolean isHiddenInToDo = false; // To-Do 리스트에서 숨김 여부 (기본값 false)
    private Boolean isReminderEnabled = true; // 리마인더 활성화 여부 (기본값 true)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // ScheduleDTO -> Schedule 변환 메서드
    public static Schedule fromDTO(String firebaseUid, ScheduleDTO dto) {
        return Schedule.builder()
                .firebaseUid(firebaseUid)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .categoryId(dto.getCategoryId())
                .build();
    }

    // 일정 미루기 메서드 (일 수 기준으로 연기)
    public void postponeSchedule(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("미룰 일 수는 0보다 커야 합니다.");
        }
        if (this.endTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("이미 지난 일정은 미룰 수 없습니다.");
        }
        this.startTime = this.startTime.plusDays(days);
        this.endTime = this.endTime.plusDays(days);
    }

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminders = new ArrayList<>();

    // 리마인더 추가 메서드
    public void addReminder(int minutesBefore) {
        this.reminders.add(new Reminder(this, minutesBefore));
    }

    // 특정 리마인더 삭제 메서드
    public void removeReminder(int minutesBefore) {
        this.reminders.removeIf(reminder -> reminder.getMinutesBefore() == minutesBefore);
    }

    // To-Do 리스트에서 숨기면서 리마인더 비활성화
    public void hideFromToDoList() {
        this.isHiddenInToDo = true;
        this.isReminderEnabled = false; // 리마인더도 비활성화
    }

}
