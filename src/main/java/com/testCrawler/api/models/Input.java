package com.testCrawler.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Input {
    private String companyName;
    private String phone;
    private String website;
    private String facebook;
}
