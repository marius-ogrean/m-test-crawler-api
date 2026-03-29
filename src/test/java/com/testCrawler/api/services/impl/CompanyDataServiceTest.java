package com.testCrawler.api.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyDataServiceTest {

    @Test
    public void getDomainTest() {
        var companyDataService = new CompanyDataServiceImpl(null);

        var domain = companyDataService.getDomain("https://https//acornlawpc.com/");
        Assertions.assertEquals("acornlawpc.com", domain);

        domain = companyDataService.getDomain("http://sbstransportllc.com/index.html?lang=en");
        Assertions.assertEquals("sbstransportllc.com", domain);

        domain = companyDataService.getDomain("google.com");
        Assertions.assertEquals("google.com", domain);

        domain = companyDataService.getDomain(null);
        Assertions.assertNull(domain);
    }
}
