package com.testCrawler.api.services;

import com.testCrawler.api.models.FillRate;

public interface CompanyDataService {
    void uploadCompanyNamesFromFile();
    Long getCoverage();
    FillRate getFillRate();
}
