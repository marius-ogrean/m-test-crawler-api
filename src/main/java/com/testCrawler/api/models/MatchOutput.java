package com.testCrawler.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MatchOutput {
    private Input input;
    private CompanyDocument companyDocument;
}
