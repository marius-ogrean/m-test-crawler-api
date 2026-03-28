package com.testCrawler.api.services.impl;

import com.testCrawler.api.services.SolrService;
import com.testCrawler.api.models.CompanyDocument;
import com.testCrawler.api.models.RetrievalResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SolrServiceImpl implements SolrService {
    private static final Logger LOG = LoggerFactory.getLogger(SolrServiceImpl.class);

    private final SolrClient solrClient;
    private final String solrCollection = "companies";

    public SolrServiceImpl(@Value("${solr.url}") String solrUrl) {
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

    public void createCompanyDocument(String domain, String commercialName, String legalName,
                                      List<String> allAvailableNames) {
        var companyDocument = CompanyDocument.builder()
                .id(domain)
                .commercialName(StringUtils.hasText(commercialName) ? List.of(commercialName) : null)
                .legalName(StringUtils.hasText(legalName) ? List.of(legalName) : null)
                .allAvailableNames(allAvailableNames)
                .fromCrawl(List.of(false))
                .build();

        try {
            solrClient.addBean(solrCollection, companyDocument, 100);
        } catch (Exception ex) {
            LOG.error("Error creating document", ex);
        }
    }

    public void updateDocument(String domain, String commercialName, String legalName,
                               List<String> allAvailableNames) {
        var document = new SolrInputDocument();
        document.addField("id",domain);

        boolean shouldUpdate = false;

        if (StringUtils.hasText(commercialName)) {
            var fieldModifier = new HashMap<String, Object>();
            fieldModifier.put("set", List.of(commercialName));

            document.addField("commercialName", fieldModifier);
            shouldUpdate = true;
        }

        if (StringUtils.hasText(legalName)) {
            var fieldModifier = new HashMap<String, Object>();
            fieldModifier.put("set", List.of(legalName));

            document.addField("legalName", fieldModifier);
            shouldUpdate = true;
        }

        if (allAvailableNames != null && !allAvailableNames.isEmpty()) {
            var fieldModifier = new HashMap<String, Object>();
            fieldModifier.put("set", allAvailableNames);

            document.addField("allAvailableNames", fieldModifier);
            shouldUpdate = true;
        }

        if (!shouldUpdate) {
            LOG.info("No update done");
            return;
        }

        try {
            solrClient.add(solrCollection, document, 100);
        } catch (Exception ex) {
            LOG.error("Error updating doc", ex);
            try {
                solrClient.add(solrCollection, document, 100);
            } catch (Exception ex1) {
                LOG.error("Error updating doc retry", ex1);
            }
        }
    }

    @Override
    public Long getCountFromQuery(String query) {
        final Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("q", query);
        var queryParams = new MapSolrParams(queryParamMap);

        try {
            QueryResponse response = solrClient.query(solrCollection, queryParams);

            return response.getResults().getNumFound();
        } catch (Exception ex) {
            LOG.error("Error retrieving document", ex);

            throw new RuntimeException(ex);
        }
    }
}
