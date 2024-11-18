package com.vir.postmanbe.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RestApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> sendRequest(String url, HttpMethod method, Map<String, String> headers, Map<String, String> params, String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::set);

        if (!httpHeaders.containsKey("Accept")) {
            httpHeaders.setAccept(MediaType.parseMediaTypes("application/json"));
        }

        HttpEntity<String> entity;
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
            entity = new HttpEntity<>(body, httpHeaders);
        } else {
            entity = new HttpEntity<>(httpHeaders);
        }

        return restTemplate.exchange(url, method, entity, String.class, params);
    }
}
