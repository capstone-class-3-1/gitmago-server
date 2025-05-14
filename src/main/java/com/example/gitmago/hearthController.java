package com.example.gitmago;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hearthController {
    @GetMapping("/hearth")
    public ResponseEntity<String> hearthendpoint() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/")
    public ResponseEntity<String> rootendpoint() {
        return ResponseEntity.ok("OK");
    }
}

