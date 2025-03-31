package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.repository.ReminderRepository;
import org.example.service.ReminderService;
import org.example.entity.Reminder;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderInitializer {
    private final ReminderRepository reminderRepository;
    private final ReminderService reminderService;

    @EventListener(ApplicationReadyEvent.class) // 서버 시작 시 실행
    public void initReminders() {
        List<Reminder> reminders = reminderRepository.findAll();
        for (Reminder reminder : reminders) {
            reminderService.scheduleReminder(reminder);
        }
    }
}
