package io.howstheairtoday.airqualityclient.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {

        LOGGER.info("Request URL: {}", request.getURI());
        LOGGER.info("Request Method: {}", request.getMethod());
        LOGGER.info("Request Headers: {}", request.getHeaders());
        LOGGER.info("Request Body: {}", new String(body));
    }

    private void logResponse(ClientHttpResponse response) throws IOException {

        LOGGER.info("Response Status Code: {}", response.getStatusCode());
        LOGGER.info("Response Status Text: {}", response.getStatusText());
        LOGGER.info("Response Headers: {}", response.getHeaders());
        LOGGER.info("Response Body: {}", response.getBody());
    }

}
