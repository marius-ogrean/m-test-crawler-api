package com.testCrawler.api.services.impl;

import com.testCrawler.api.models.Coverage;
import com.testCrawler.api.models.FillRate;
import com.testCrawler.api.services.CompanyDataService;
import com.testCrawler.api.services.SolrService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public Coverage getCoverage() {
        var crawledCount = solrService.getCountFromQuery("fromCrawl:(true)");
        var notCrawledCount = solrService.getCountFromQuery("fromCrawl:(false)");
        var total = crawledCount + notCrawledCount;

        BigDecimal percentage = BigDecimal.ZERO;
        if (total != 0) {
            percentage = BigDecimal.valueOf(crawledCount)
                    .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return Coverage.builder()
                .crawled(crawledCount)
                .totalWebsites(total)
                .percentage(percentage)
                .build();
    }

    @Override
    public FillRate getFillRate() {
        var allDocuments = solrService.getAllDocumentsFromQuery("fromCrawl:(true)");

        var fillRate = new FillRate();

        fillRate.setCrawled(allDocuments.size());

        int totalDataPoints = 0;

        for (var companyDocument : allDocuments) {
            if (companyDocument.getPhoneData() != null) {
                totalDataPoints += companyDocument.getPhoneData().size();

                if (companyDocument.getPhoneData().get(0).equals("#")) {
                    totalDataPoints--;
                }
            }

            if (companyDocument.getSocialsData() != null) {
                totalDataPoints += companyDocument.getSocialsData().size();

                if (companyDocument.getSocialsData().get(0).equals("#")) {
                    totalDataPoints--;
                }
            }

            if (companyDocument.getAddressData() != null) {
                totalDataPoints += companyDocument.getAddressData().size();

                if (companyDocument.getAddressData().get(0).equals("#")) {
                    totalDataPoints--;
                }
            }
        }

        BigDecimal dataPointsPerWebsite = BigDecimal.ZERO;
        if (!allDocuments.isEmpty()) {
            dataPointsPerWebsite = BigDecimal.valueOf(totalDataPoints)
                    .divide(BigDecimal.valueOf(allDocuments.size()), 2, RoundingMode.HALF_UP);
        }

        fillRate.setTotalDataPoints(totalDataPoints);
        fillRate.setDataPointsPerWebsite(dataPointsPerWebsite);

        return fillRate;
    }
}
