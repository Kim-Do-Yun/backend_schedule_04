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
import org.springframework.context.annotation.DependsOn;

@Service
@RequiredArgsConstructor
@DependsOn("FCMService")
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final FCMService fcmService;  // FCM 서비스 주입
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void saveReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    // 새로운 리마인더 예약 (Schedule과 minutesBefore를 받아 Reminder 객체 생성)
    public void scheduleReminder(Schedule schedule, int minutesBefore) {
        Reminder reminder = new Reminder(schedule, minutesBefore);
        saveReminder(reminder);
        scheduleReminder(reminder);  // 기존 메서드 호출
    }


    // 🔔 일정의 기본 알림 예약 (startTime에 맞춰 자동 설정)
    public void scheduleDefaultReminder(Schedule schedule) {
        System.out.println("📌 기본 알림 예약: " + schedule.getTitle() + ", 시작 시간: " + schedule.getStartTime());

        if (schedule.getStartTime() == null) {
            System.err.println("🚨 오류: 일정의 startTime이 NULL입니다!");
            return; // startTime이 없으면 예약할 수 없으므로 종료
        }

        scheduleReminder(schedule, 0);
    }


    // 🔔 사용자가 추가한 리마인더 예약
    public void scheduleAdditionalReminders(Schedule schedule, List<Integer> reminderTimes) {
        for (int minutesBefore : reminderTimes) {
            scheduleReminder(schedule, minutesBefore);
        }
    }

    // 기존 알림을 복원하는 메서드 (앱 실행 시 호출)
    @PostConstruct
    public void restoreReminders() {
        List<Reminder> reminders = reminderRepository.findAll();
        for (Reminder reminder : reminders) {
            if (reminder == null || reminder.getSchedule() == null) {
                System.err.println("🚨 잘못된 리마인더 감지: " + reminder);
                continue;
            }

            LocalDateTime reminderTime = reminder.getSchedule().getStartTime().minusMinutes(reminder.getMinutesBefore());
            if (reminderTime.isBefore(LocalDateTime.now())) {
                // 이미 지난 리마인더는 삭제
                reminderRepository.delete(reminder);
                System.out.println("🗑️ 지난 리마인더 삭제: " + reminder);
                continue;
            }

            scheduleReminder(reminder);
        }
    }


    // 새로운 리마인더 예약
    public void scheduleReminder(Reminder reminder) {
        if (reminder == null || reminder.getSchedule() == null) {
            System.err.println("🚨 잘못된 리마인더: reminder 또는 schedule이 null입니다.");
            return;
        }

        LocalDateTime reminderTime = reminder.getSchedule().getStartTime().minusMinutes(reminder.getMinutesBefore());
        long delay = Duration.between(LocalDateTime.now(), reminderTime).toMillis();

        if (delay > 0) {
            scheduler.schedule(() -> sendNotification(reminder.getSchedule(), reminder.getMinutesBefore()), delay, TimeUnit.MILLISECONDS);
        } else {
            System.out.println("⏳ 알림 시간이 이미 지나 삭제됨: " + reminderTime);
            reminderRepository.delete(reminder);
        }
    }


    private void sendNotification(Schedule schedule, int minutesBefore) {
        String firebaseUid = schedule.getFirebaseUid();
        String title = "📅 일정 알림";
        String message = "🔔 " + schedule.getTitle() + " - " + minutesBefore + "분 전!";

        fcmService.sendPushNotification(firebaseUid, title, message);
    }


}
