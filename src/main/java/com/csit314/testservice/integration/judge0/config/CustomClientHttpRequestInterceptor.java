package com.csit314.testservice.integration.judge0.config;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final Logger LOG = LoggerFactory.getLogger(CustomClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] bytes, ClientHttpRequestExecution execution) throws IOException {

        // log the http request
        LOG.info("URI: {}", request.getURI());
        LOG.info("HTTP Method: {}", request.getMethodValue());
        LOG.info("HTTP Headers: {}", request.getHeaders());

        return execution.execute(request, bytes);
    }

}
