package com.csit314.testservice.integration.judge0;

import com.csit314.testservice.integration.judge0.dto.request.SubmissionBatchRequestDto;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionRequestDto;
import com.csit314.testservice.integration.judge0.dto.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Component
public class Judge0ServiceIntegration {
    @Value("${judge0.service.base.url}")
    private String judge0baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    private void configBaseUrl() {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(judge0baseUrl));
    }


    public SubmissionVerdictResponseDto executeTestCase(String code, String input, String expectedOutput) throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", "471015e19cmsh7fe384858bf8c86p1d65e2jsn8c49a074a372");
        ResponseEntity<SubmissionTokenResponseDto> tokenResponseDto = restTemplate
                .postForEntity(judge0baseUrl + "/submissions?base64_encoded=false", new HttpEntity<>(SubmissionRequestDto.builder().source_code(code).language_id(52).stdin(input).expected_output(expectedOutput).build(), headers),
                        SubmissionTokenResponseDto.class);
        if (tokenResponseDto.getStatusCode().is2xxSuccessful()) {
            String token = Objects.requireNonNull(tokenResponseDto.getBody()).getToken();

            /*Get the token for this submission*/
            ResponseEntity<SubmissionVerdictResponseDto> submissionVerdict = restTemplate
                    .exchange(judge0baseUrl + "/submissions/" + token+ "?base64_encoded=false&fields=stdout,stdin,status", HttpMethod.GET,
                            new HttpEntity<>(null, headers), SubmissionVerdictResponseDto.class);

            if(submissionVerdict.getStatusCode().is2xxSuccessful()){
                /*If the code is in queue or in processing, send the request again*/
                while (submissionVerdict.getBody().getStatus().getId() == 1 || submissionVerdict.getBody().getStatus().getId() == 2){
                    Thread.sleep(1000);
                    submissionVerdict = restTemplate
                            .exchange(judge0baseUrl + "/submissions/" + token+ "?base64_encoded=false&fields=stdout,stdin,status", HttpMethod.GET,
                                    new HttpEntity<>(null, headers), SubmissionVerdictResponseDto.class);
                }
                return submissionVerdict.getBody();
            }
            else{
                throw new RuntimeException("Error");

            }

        }
        else{
            throw new RuntimeException("Error");
        }

    }

    public SubmissionBatchResponseDto getExpectedOutputBatch(SubmissionBatchRequestDto submissionBatchRequestDto) throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", "471015e19cmsh7fe384858bf8c86p1d65e2jsn8c49a074a372");

        ResponseEntity<SubmissionTokenResponseDto[]> submissionBatchTokenResponseDtoResponseEntity = restTemplate
                .postForEntity(judge0baseUrl + "/submissions/batch?base64_encoded=false", new HttpEntity<>(submissionBatchRequestDto, headers),
                        SubmissionTokenResponseDto[].class);


        if (submissionBatchTokenResponseDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
            SubmissionTokenResponseDto[] responseTokens = submissionBatchTokenResponseDtoResponseEntity.getBody();
            StringBuilder tokenParam = new StringBuilder();

            /*Construct token list for batch submission*/
            for (int i = 0; i < Objects.requireNonNull(responseTokens).length - 1; i++) {
                tokenParam.append(responseTokens[i].getToken()).append(",");
            }
            tokenParam.append(responseTokens[responseTokens.length - 1].getToken());


            ResponseEntity<SubmissionBatchResponseDto> submissionBatchResponseDtoResponseEntity = restTemplate
                    .exchange(judge0baseUrl + "/submissions/batch?tokens=" + tokenParam.toString() + "&base64_encoded=false&fields=stdin,stdout", HttpMethod.GET,
                            new HttpEntity<>(null, headers), SubmissionBatchResponseDto.class);

            if (submissionBatchResponseDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
                while (isOneStdOutNull(Objects.requireNonNull(submissionBatchResponseDtoResponseEntity.getBody()))) {
                    /*Retry after 2 seconds*/
                    Thread.sleep(2000);
                    submissionBatchResponseDtoResponseEntity = restTemplate
                            .exchange(judge0baseUrl + "/submissions/batch?tokens=" + tokenParam.toString() + "&base64_encoded=false&fields=stdin,stdout", HttpMethod.GET,
                                    new HttpEntity<>(null, headers), SubmissionBatchResponseDto.class);
                }
                return submissionBatchResponseDtoResponseEntity.getBody();
            } else {
                throw new RuntimeException("Error");
            }

        } else {
            throw new RuntimeException("Error");
        }
    }

    private boolean isOneStdOutNull(SubmissionBatchResponseDto responseDto) {
        for (SubmissionResponseDto res : responseDto.getSubmissions()) {
            if (res.getStdout() == null) {
                return true;
            }
        }
        return false;
    }
}
