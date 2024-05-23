package com.example.Kiosk.user;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    private final UserService userService;
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

    @PostMapping("/passwordIncorrect")
    public ResponseEntity<Map<String, Boolean>> validatePasswordIncorrect(@RequestParam String password, @RequestParam String target_password) {
        Map<String, Boolean> response = new HashMap<>();
        boolean isValid = validatePasswordIncorrectLogic(password, target_password);
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }

    private boolean validatePasswordIncorrectLogic(String password, String targetPassword) {
        // 비밀번호가 null이거나 빈 문자열인 경우 유효하지 않음
        if (password == null || password.isEmpty()) {
            return false;
        }
        // 비밀번호와 대상 비밀번호가 같은 경우 유효함
        return password.equals(targetPassword);
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

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm){
        return "signup_form";
    }


    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError usernameError = bindingResult.getFieldError("username");
            FieldError password1Error = bindingResult.getFieldError("password1");
            FieldError password2Error = bindingResult.getFieldError("password2");
            FieldError emailError = bindingResult.getFieldError("email");

            if (usernameError != null) {
                userCreateForm.setUsernameError(usernameError.getDefaultMessage());
            }
            if (password1Error != null) {
                userCreateForm.setPassword1Error(password1Error.getDefaultMessage());
            }
            if (password2Error != null) {
                userCreateForm.setPassword2Error(password2Error.getDefaultMessage());
            }
            if (emailError != null) {
                userCreateForm.setEmailError(emailError.getDefaultMessage());
            }

            return "signup_form";
        }
        userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getEmail());

        return "redirect:/";
    }
}
