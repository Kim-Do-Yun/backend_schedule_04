package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ReminderDTO;
import org.example.entity.Schedule;
import org.example.service.ReminderService;
import org.example.repository.ScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final ScheduleRepository scheduleRepository;

    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody ReminderDTO dto) {
        try {
            // 📌 1. Schedule 저장
            Schedule schedule = Schedule.builder()
                    .title(dto.getTitle())
                    .startTime(LocalDateTime.parse(dto.getStartTime()))
                    .endTime(LocalDateTime.parse(dto.getEndTime()))
                    .isRecurring(dto.isRecurring())
                    .recurrenceDays(dto.getRecurrenceDays())
                    .firebaseUid("dummy")
                    .build();

            schedule = scheduleRepository.save(schedule);

            // 📌 2. 리마인더 시간 설정
            List<Integer> reminderTimes = dto.getReminderMinutesBeforeList();
            if (reminderTimes != null && !reminderTimes.isEmpty()) {
                for (int minutesBefore : reminderTimes) {
                    reminderService.createAndScheduleReminder(schedule, minutesBefore);
                }
            } else {
                reminderService.scheduleDefaultReminder(schedule);
            }


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("리마인더 생성 실패: " + e.getMessage());
        }
    }
}
