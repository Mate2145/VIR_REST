package com.vir.postmanbe.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tester")
@Slf4j
public class RestApiController {

    @PostMapping("/send")
    public ResponseEntity<String> sendRequest(
            @RequestParam String url,
            @RequestParam HttpMethod method,
            @RequestBody Map<String, String> headers,
            @RequestBody Map<String, String> params) {

        log.info("Received a request to send a {} request to URL: {}", method, url);
        log.debug("Headers: {}", headers);
        log.debug("Params: {}", params);
        log.info("Request sent successfully to {}", url);

        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/greet")
    public ResponseEntity<String> testRequest() {

        log.info("Received a GET request for /greet endpoint");

        return ResponseEntity.ok().body("Hello, world!");
    }

}
