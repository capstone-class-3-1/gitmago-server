package com.example.gitmago.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hearthController {
    @GetMapping("/hearth")
    public ResponseEntity<String> hearth() {
        return ResponseEntity.ok("OK");
    }
}
