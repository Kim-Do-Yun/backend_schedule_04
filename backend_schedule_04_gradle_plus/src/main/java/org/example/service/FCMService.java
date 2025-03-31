package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class FCMService {
    public void sendPushNotification(String firebaseUid, String message) {
        System.out.println("푸시 알림 전송: " + firebaseUid + " - " + message);
        // Firebase 연동 코드 작성 필요
    }
}
