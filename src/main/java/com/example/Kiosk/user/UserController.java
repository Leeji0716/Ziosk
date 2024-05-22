package com.example.Kiosk.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @PostMapping("/username")
    public ResponseEntity<Map<String, Boolean>> validateUsername(@RequestParam String username){
        Map<String, Boolean> response = new HashMap<>();
        boolean isValid = validateUsernameLogic(username);
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password")
    public ResponseEntity<Map<String, Boolean>> validatePassword(@RequestParam String password) {
        Map<String, Boolean> response = new HashMap<>();
        boolean isValid = validatePasswordLogic(password);
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }

    // 사용자 이름 유효성 검사 로직
    private boolean validateUsernameLogic(String username) {
        return username != null && username.length() >= 5;
    }

    // 비밀번호 유효성 검사 로직
    private boolean validatePasswordLogic(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasDigit = false;
        for (char ch : password.toCharArray()) {
            if (Character.isDigit(ch)) {
                hasDigit = true;
            }
        }
        return hasDigit;
    }
}
