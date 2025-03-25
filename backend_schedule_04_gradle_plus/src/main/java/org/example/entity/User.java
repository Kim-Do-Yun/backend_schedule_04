package org.example.entity;

import jakarta.persistence.*; // JPA 어노테이션
import java.time.LocalDateTime; // LocalDateTime
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    private String firebaseUid; // 기본키로 설정

    private String username;
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getter & Setter
}
