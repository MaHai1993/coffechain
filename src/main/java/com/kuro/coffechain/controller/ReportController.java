package com.kuro.coffechain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    //TODO: need to implement the report here then the manager can see the report
    @GetMapping("/alls")
    public ResponseEntity<String> getReport() {
        return ResponseEntity.ok().body("Get Report successful");
    }
}
