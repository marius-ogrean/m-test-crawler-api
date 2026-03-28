package com.testCrawler.api.controllers;

import com.testCrawler.api.models.Coverage;
import com.testCrawler.api.models.FillRate;
import com.testCrawler.api.services.CompanyDataService;
import com.testCrawler.api.services.SolrService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("companyData")
@Slf4j
@AllArgsConstructor
public class CompanyDataController {

    private SolrService solrService;
    private CompanyDataService companyDataService;

    @GetMapping(value = "/document/{domain}")
    public String getDocument(@PathVariable String domain) {
        return solrService.getCompanyDocument(domain).toString();
    }

    @GetMapping(value = "/uploadCompanyNamesFromFile")
    public String uploadCompanyNamesFromFile() {
        companyDataService.uploadCompanyNamesFromFile();
        return "Upload complete";
    }

    @GetMapping(value = "/coverage")
    public Coverage getCoverage() {
        return companyDataService.getCoverage();
    }

    @GetMapping(value = "/fillRate")
    public FillRate getFillRate() {
        return companyDataService.getFillRate();
    }
}
