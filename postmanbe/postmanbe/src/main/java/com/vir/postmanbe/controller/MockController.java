package com.vir.postmanbe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getMock() {
        return ResponseEntity.ok(Map.of("method", "GET", "message", "GET request received"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> postMock(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "method", "POST",
                "message", "POST request received",
                "receivedBody", body.toString()
        ));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> putMock(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of(
                "method", "PUT",
                "message", "PUT request received",
                "receivedBody", body.toString()
        ));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteMock() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                "method", "DELETE",
                "message", "DELETE request received"
        ));
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> patchMock(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of(
                "method", "PATCH",
                "message", "PATCH request received",
                "receivedBody", body.toString()
        ));
    }
}
