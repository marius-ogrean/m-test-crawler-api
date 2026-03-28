package com.testCrawler.api;

import com.testCrawler.api.services.SolrService;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SolrServiceTest {

    //@Test
    public void createDocumentTest() {
        var solrService = new SolrService("http://localhost:8983/solr");

        solrService.createCompanyDocument("bostonzen.org", "Greater Boston Zen Center",
                null, null);

        solrService.updateDocument("bostonzen.org", null, "GREATER BOSTON ZEN CENTER INC.",
                List.of("Greater Boston Zen Center", "Boston Zen", "GREATER BOSTON ZEN CENTER INC."));

        //var document = solrService.getCompanyDocument("bostonzen.org");
    }
}
