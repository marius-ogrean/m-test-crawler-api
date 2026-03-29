package com.testCrawler.api.services;

import com.testCrawler.api.models.Coverage;
import com.testCrawler.api.models.FileMatchOutput;
import com.testCrawler.api.models.FillRate;
import com.testCrawler.api.models.Input;
import com.testCrawler.api.models.MatchOutput;

public interface CompanyDataService {
    void uploadCompanyNamesFromFile();
    Coverage getCoverage();
    FillRate getFillRate();
    MatchOutput matchInput(Input input);
    FileMatchOutput matchInputFromFile();
}
