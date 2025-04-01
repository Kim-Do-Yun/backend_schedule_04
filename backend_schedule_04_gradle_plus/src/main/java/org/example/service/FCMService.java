package org.example.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FCMService {

    // Firebase 초기화 (서비스 계정 키 파일 사용)
    public FCMService() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("C:\\Firebaseservicekeys\\shcedule04-firebase-adminsdk-fbsvc-a4866598d2.json"); // 경로가 올바른지 확인

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount)) // GoogleCredentials로 변경
                .build();

        // Firebase 초기화 (앱이 아직 초기화되지 않았다면 초기화)
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    // 수정된 메서드: 2개의 매개변수
    public void sendPushNotification(String firebaseUid, String title, String message) {
        // Notification 객체에 sound 추가하지 않음. 'sound' 속성은 payload에서 처리
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();

        Message fcmMessage = Message.builder()
                .setToken(firebaseUid) // FCM 토큰
                .setNotification(notification)
                .putData("sound", "default") // sound 속성은 data payload로 설정
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            System.out.println("✅ 푸시 알림 전송 성공: " + response);
        } catch (Exception e) {
            System.err.println("🚨 푸시 알림 전송 실패: " + e.getMessage());
        }
    }
}
