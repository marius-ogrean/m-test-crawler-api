package com.testCrawler.api.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dataRetrieval")
@Slf4j
@AllArgsConstructor
public class DataRetrievalController {

    @GetMapping
    public String getCoverage() {
        return "coverage1";
    }
}
