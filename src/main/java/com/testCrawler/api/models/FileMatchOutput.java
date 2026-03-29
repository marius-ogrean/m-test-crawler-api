package com.testCrawler.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileMatchOutput {
    private int numberOfInputs;
    private int numberOfMatches;
    private int numberOfCrawledWebsites;
    private List<MatchOutput> matches;
    private List<MatchOutput> nonMatches;
}
