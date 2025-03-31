package org.example.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.example.entity.Reminder;
import org.example.entity.Schedule;
import org.springframework.stereotype.Service;
import org.example.repository.ReminderRepository;

import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final FCMService fcmService;  // FCM 서비스 주입
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void saveReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    // 기존 알림을 복원하는 메서드 (앱 실행 시 호출)
    @PostConstruct
    public void restoreReminders() {
        List<Reminder> reminders = reminderRepository.findAll();
        for (Reminder reminder : reminders) {
            if (reminder != null && reminder.getSchedule() != null) {
                scheduleReminder(reminder);
            } else {
                System.err.println("🚨 잘못된 리마인더 감지: " + reminder);
            }
        }
    }


    // 새로운 리마인더 예약
    public void scheduleReminder(Reminder reminder) {
        if (reminder == null || reminder.getSchedule() == null) {
            System.err.println("🚨 잘못된 리마인더: reminder 또는 schedule이 null입니다.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = reminder.getSchedule().getStartTime().minusMinutes(reminder.getMinutesBefore());

        long delay = Duration.between(now, reminderTime).toMillis();
        if (delay > 0) {
            scheduler.schedule(() -> sendNotification(reminder.getSchedule(), reminder.getMinutesBefore()), delay, TimeUnit.MILLISECONDS);
        } else {
            System.err.println("⏳ 알림 시간이 이미 지남: " + reminderTime);
        }
    }


    private void sendNotification(Schedule schedule, int minutesBefore) {
        String message = "🔔 " + schedule.getTitle() + " - " + minutesBefore + "분 전!";
        fcmService.sendPushNotification(schedule.getFirebaseUid(), message);
    }

}
