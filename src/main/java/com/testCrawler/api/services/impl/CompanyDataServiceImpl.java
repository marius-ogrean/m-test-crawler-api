package com.testCrawler.api.services.impl;

import com.testCrawler.api.services.CompanyDataService;
import com.testCrawler.api.services.SolrService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyDataServiceImpl implements CompanyDataService {
    private static final Logger LOG = LoggerFactory.getLogger(CompanyDataServiceImpl.class);
    private SolrService solrService;

    @Override
    public void uploadCompanyNamesFromFile() {
        var classloader = Thread.currentThread().getContextClassLoader();
        try (var inputStream = classloader.getResourceAsStream("sample-websites-company-names.csv");
             var streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             var reader = new BufferedReader(streamReader)) {
            reader.readLine();

            while (true) {
                var line = reader.readLine();

                if (line == null) {
                    break;
                }

                var lineParts = line.split(",");

                var domain = lineParts[0];

                if (!StringUtils.hasText(domain)) {
                    continue;
                }

                List<String> allAvailableNames = null;
                if (StringUtils.hasText(lineParts[3])) {
                    allAvailableNames = new ArrayList<>();

                    var names = lineParts[3].split("\\|");

                    for (var name : names) {
                        allAvailableNames.add(name.trim());
                    }
                }

                var retrievalResult = solrService.getCompanyDocument(domain);

                if (retrievalResult.isHasError()) {
                    LOG.error("Error retrieving domain: {}", domain);
                    continue;
                }

                if (retrievalResult.getCompanyDocument() == null) {
                    solrService.createCompanyDocument(domain, lineParts[1], lineParts[2], allAvailableNames);
                } else {
                    solrService.updateDocument(domain, lineParts[1], lineParts[2], allAvailableNames);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long getCoverage() {
        var count = solrService.getCountFromQuery("fromCrawl:(true)");
        return count;
    }
}
