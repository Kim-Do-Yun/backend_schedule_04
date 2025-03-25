package org.example.controller;

import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
        String firebaseUid = request.get("firebaseUid");
        if (firebaseUid == null) {
            return ResponseEntity.badRequest().body("Firebase UID is required");
        }
        userService.saveUser(firebaseUid);
        return ResponseEntity.ok("User registered successfully");
    }
}
