package com.testCrawler.api.controllers;

import com.testCrawler.api.services.SolrService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dataRetrieval")
@Slf4j
@AllArgsConstructor
public class DataRetrievalController {

    private SolrService solrService;

    @GetMapping(value = "/document/{domain}")
    public String getDocument(@PathVariable String domain) {
        return solrService.getCompanyDocument(domain).toString();
    }
}
