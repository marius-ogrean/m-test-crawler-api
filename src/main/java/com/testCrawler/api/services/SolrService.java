package com.testCrawler.api.services;

import com.testCrawler.api.models.RetrievalResult;

import java.util.List;

public interface SolrService {
    RetrievalResult getCompanyDocument(String domain);
    void createCompanyDocument(String domain, String commercialName, String legalName,
                               List<String> allAvailableNames);
    void updateDocument(String domain, String commercialName, String legalName,
                        List<String> allAvailableNames);
    Long getCountFromQuery(String query);
}
