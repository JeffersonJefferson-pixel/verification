package com.example.verification.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class ResourceController {

  @GetMapping("")
  public ResponseEntity<Object> getResource() {
    Map<String, String> body = new HashMap<>();
    body.put("message", "Resource acquired.");

    return new ResponseEntity<>(body, HttpStatus.OK);
  }
}
