package com.testCrawler.api.services;

import com.testCrawler.api.models.CompanyDocument;
import com.testCrawler.api.models.RetrievalResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SolrService {
    private static final Logger LOG = LoggerFactory.getLogger(SolrService.class);

    private final SolrClient solrClient;
    private final String solrCollection = "companies";

    public SolrService(@Value("${solr.url}") String solrUrl) {
        solrClient = new HttpJdkSolrClient.Builder(solrUrl).build();
    }

    public RetrievalResult getCompanyDocument(String domain) {
        final Map<String, String> queryParamMap = new HashMap<>();
        var query = String.format("id:\"%s\"", domain);
        queryParamMap.put("q", query);
        var queryParams = new MapSolrParams(queryParamMap);

        var result = new RetrievalResult();

        try {
            QueryResponse response = solrClient.query(solrCollection, queryParams);
            var documents = response.getBeans(CompanyDocument.class);

            if (documents.isEmpty()) {
                return result;
            }

            var document = documents.get(0);
            result.setCompanyDocument(document);
            return result;
        } catch (Exception ex) {
            LOG.error("Error retrieving document", ex);

            result.setHasError(true);
            return result;
        }
    }
}
