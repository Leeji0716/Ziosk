package com.example.Kiosk;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class MainController {
    @GetMapping("/")
    public String root(){
        return "main";
    }

    @GetMapping("/test")
    public String test() {
        return "test.html";
    }

    @GetMapping("/ajax-test")
    @ResponseBody
    public ResponseEntity<?> test2() {

        Person p1 = new Person();
        p1.setName("john");
        p1.setAge(20);

        return ResponseEntity.ok(p1);
    }

}
