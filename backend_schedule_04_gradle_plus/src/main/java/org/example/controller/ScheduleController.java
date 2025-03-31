package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ScheduleDTO;
import org.example.entity.Schedule;
import org.example.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 추가
    @PostMapping
    public ResponseEntity<Schedule> addSchedule(
            @RequestHeader("firebaseUid") String firebaseUid,
            @RequestBody ScheduleDTO dto,
            @RequestParam List<Integer> reminderTimes) {
        return ResponseEntity.ok(scheduleService.addSchedule(firebaseUid, dto, reminderTimes));
    }

    @GetMapping("/todo")
    public ResponseEntity<List<Schedule>> getToDoList(@RequestHeader("firebaseUid") String firebaseUid) {
        return ResponseEntity.ok(scheduleService.getToDoList(firebaseUid));
    }


    // 일정 수정
    @PutMapping("/{scheduleId}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long scheduleId,
                                                   @RequestBody ScheduleDTO dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, dto));
    }

    // 일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

    // 일정 나열 (필터)
    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(@RequestHeader("firebaseUid") String firebaseUid,
                                                       @RequestParam LocalDateTime start,
                                                       @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(scheduleService.getSchedules(firebaseUid, start, end));
    }

    // 일정 미루기
    @PatchMapping("/{scheduleId}/postpone")
    public ResponseEntity<Schedule> postponeSchedule(@PathVariable Long scheduleId,
                                                     @RequestParam int days) {
        return ResponseEntity.ok(scheduleService.postponeSchedule(scheduleId, days));
    }

    // 일정에서 특정 리마인더 제거
    @PatchMapping("/{scheduleId}/reminders")
    public ResponseEntity<Schedule> removeReminder(
            @PathVariable Long scheduleId,
            @RequestParam int minutesBefore) {
        return ResponseEntity.ok(scheduleService.removeReminder(scheduleId, minutesBefore));
    }

}