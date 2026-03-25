package com.TEST.tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(BaseHttpClient.class);

    private void logResponse(Response response) {
        int responseStatusCode = response.getStatusCode();
        String responseString = response.asPrettyString();
        long responseTimeInMilliSec = response.getTimeIn(TimeUnit.MILLISECONDS);
//        test.info("Response Body: \n" + responseString);
        System.out.println("Response time: " + (double) responseTimeInMilliSec/1000 + " Seconds");
        System.out.println("Response status code: " + responseStatusCode);
        System.out.println("Response Body: \n" + responseString);
        logger.info("Response: {} {}", responseStatusCode, response.getStatusLine());
        logger.debug("Response Body: {}", response.getBody().asPrettyString());
    }

    private String appendQueryParams(String url, Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder(url).append("?");
        queryParams.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));
        return sb.substring(0, sb.length() - 1);
    }

    private String generateCURL(String method, String url, Map<String, String> headers, Map<String, String> cookies, String body) {
        StringBuilder curl = new StringBuilder("curl -X '").append(method).append("' \\\n '").append(url).append("' \\\n");

        if (headers != null) {
            headers.forEach((key, value) -> curl.append("  -H '").append(key).append(": ").append(value).append("' \\\n"));
        }

        if (cookies != null) {
            cookies.forEach((key, value) -> curl.append("  -b '").append(key).append("=").append(value).append("' \\\n"));
        }

        if (body != null) {
            curl.append("  -d '").append(body.replace("'", "\\'")).append("'\n");
        }

        return curl.toString();
    }

    // Private method for executing API requests
    private Response executeRequest(String method, String baseUrl, String endpoint, Map<String, String> headers,
                                    Map<String, String> queryParams, Map<String, String> cookies, JSONObject body) {
        try {
            RequestSpecification request = RestAssured.given().baseUri(baseUrl).basePath(endpoint);
            String fullUrl = baseUrl + endpoint;

            // Append Query Params
            if (queryParams != null && !queryParams.isEmpty()) {
                request.queryParams(queryParams);
                fullUrl = appendQueryParams(fullUrl, queryParams);
            }

            // Add Headers
            if (headers != null) {
                request.headers(headers);
            }

            // Add Cookies
            if (cookies != null) {
                request.cookies(cookies);
            }

            // Add JSON Body for PUT/PATCH/POST
            String jsonBody = null;
            if (body != null) {
                jsonBody = body.toString(4);
                request.body(jsonBody);
                logger.info("Request Body: " + jsonBody);
            }

            String curlCommand = generateCURL(method, fullUrl, headers, cookies, jsonBody);
            logger.info("Request URL: {}", fullUrl);
            logger.debug("Generated cURL:\n{}", curlCommand);
            System.out.println("Request URL: " + fullUrl);
            System.out.println("Generated cURL: \n" + curlCommand);
//            test.info("Request URL: " + fullUrl);
//            test.info("Generated cURL: \n" + curlCommand);

            // Execute Request
            Response response;
            switch (method) {
                case "POST":
                    response = request.post();
                    break;
                case "PUT":
                    response = request.put();
                    break;
                case "PATCH":
                    response = request.patch();
                    break;
                case "DELETE":
                    response = request.delete();
                    break;
                default:
                    response = request.get();
                    break;
            }

            logResponse(response);
            return response;
        } catch (Exception e) {
            logger.error("{} request failed: {}", method, e.getMessage(), e);
            throw new RuntimeException("API request failed", e);
        }
    }

    // Common GET method
    public Response get(String baseUrl, String endpoint, Map<String, String> headers, Map<String, String> queryParams, Map<String, String> cookies) {
        return executeRequest("GET", baseUrl, endpoint, headers, queryParams, cookies, null);
    }

    public Response get(String baseUrl, String endpoint, Map<String, String> headers) {
        return get(baseUrl, endpoint, headers, null, null);
    }

    public Response get(String baseUrl, String endpoint, Map<String, String> headers, Map<String, String> queryParams) {
        return get(baseUrl, endpoint, headers, queryParams, null);
    }

    // Common POST method
    public Response post(String baseUrl, String endpoint, Map<String, String> headers, JSONObject body) {
        return executeRequest("POST", baseUrl, endpoint, headers, null, null, body);
    }

    // Common PUT method with JSONObject body
    public Response put(String baseUrl, String endpoint, Map<String, String> headers, JSONObject body) {
        return executeRequest("PUT", baseUrl, endpoint, headers, null, null, body);
    }

    // Common PATCH method
    public Response patch(String baseUrl, String endpoint, Map<String, String> headers, JSONObject body) {
        return executeRequest("PATCH", baseUrl, endpoint, headers, null, null, body);
    }

    // Common DELETE method
    public Response delete(String baseUrl, String endpoint, Map<String, String> headers, JSONObject body) {
        return executeRequest("DELETE", baseUrl, endpoint, headers, null, null, body);
    }

    // ==============================================================================================
}
