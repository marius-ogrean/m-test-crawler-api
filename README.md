Steps to create the image:
- 

1. In the root folder run "mvn clean package"
2. Run the script create_image.cmd

Details:
-

The api has the following endpoints:

1. GET http://localhost:4444/companyData/document/{domain} - to retrieve the data for a specific domain
2. GET http://localhost:4444/companyData/uploadCompanyNamesFromFile - to seed the data from the company names file; it is a GET for ease of use in a browser
3. GET http://localhost:4444/companyData/coverage - see the total number of websites and how many were crawled
4. GET http://localhost:4444/companyData/fillRate - see the number of crawled websites and the total data points

You can see more details in the Swagger page: http://localhost:4444/swagger-ui/index.html#/

In order to be able to use the api, after running the two commands above please follow the steps from the project m-test-crawler