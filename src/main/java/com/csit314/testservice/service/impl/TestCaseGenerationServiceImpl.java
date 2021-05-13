package com.csit314.testservice.service.impl;

import com.csit314.testservice.config.CachedAssignment;
import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.integration.judge0.Judge0ServiceIntegration;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionBatchRequestDto;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionRequestDto;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionBatchResponseDto;
import com.csit314.testservice.service.TestCaseGenerationService;
import com.csit314.testservice.service.constants.SourceCodeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TestCaseGenerationServiceImpl implements TestCaseGenerationService {
    private final TestCaseMapperImpl testCaseMapper;
    private final Judge0ServiceIntegration judge0ServiceIntegration;
    private final String ASSIGNMENT_CACHE_KEY = "CSCI203_ASSIGNMENT3";
    private final RedisTemplate<String, CachedAssignment> assignmentCache;

    @PostConstruct
    public void init() {
        if (assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY)) {
            assignmentCache.delete(ASSIGNMENT_CACHE_KEY);
        }
    }

    @Override
    public List<CachedTestCase> generateTestCase() throws InterruptedException {
        final ValueOperations<String, CachedAssignment> operations = assignmentCache.opsForValue();
        if (!assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY)) {
            operations.set(ASSIGNMENT_CACHE_KEY, CachedAssignment.builder().assignmentName(ASSIGNMENT_CACHE_KEY).code(SourceCodeConstants.CORRECT_SOURCE_CODE).build());
        }
        if (assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY) && (operations.get(ASSIGNMENT_CACHE_KEY)).getCachedTestCases() != null) {
            return (Objects.requireNonNull(operations.get(ASSIGNMENT_CACHE_KEY)).getCachedTestCases());
        }
        SubmissionBatchRequestDto submissionBatchRequestDto = new SubmissionBatchRequestDto();
        /*Generate test cases*/
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(mediumInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(mediumInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(mediumInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(mediumInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(mediumInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(bigInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(bigInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(bigInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(bigInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(bigInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(bigInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(smallInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(smallInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(smallInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(smallInputGenerator()).language_id(52).build());
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(edgeCaseInputGenerator()).language_id(52).build());
        SubmissionBatchResponseDto submissionBatchResponseDto = judge0ServiceIntegration.getExpectedOutputBatch(submissionBatchRequestDto);
        CachedAssignment updatedCachedAssignment = CachedAssignment.builder().cachedTestCases(testCaseMapper.fromSubmissionBatchResponseToCachedTestCase(submissionBatchResponseDto)).assignmentName(ASSIGNMENT_CACHE_KEY).code(SourceCodeConstants.CORRECT_SOURCE_CODE).build();

        assignmentCache.delete(ASSIGNMENT_CACHE_KEY);
        operations.set(ASSIGNMENT_CACHE_KEY, updatedCachedAssignment);
        return updatedCachedAssignment.getCachedTestCases();
    }

    /*TODO: Gen 5 medium Input, 4 small inputs, and 5 big input, 1 edge case*/
    private String mediumInputGenerator() {
        return inputGenerator(70);
    }

    private String bigInputGenerator() {
        return inputGenerator(310);
    }

    private String smallInputGenerator() {
        return inputGenerator(30);
    }

    private String edgeCaseInputGenerator() {
        return "0 0\n0 0";
    }
    /*hardcoded input to feed system for testing purpose, not for production*/
    public String inputGenerator(int maxVertex) { // undirected graph with no loop
//        maxVertex = maximum number of vertex this graph can have
//        init variable
        StringBuilder input = new StringBuilder();
        Random rand = new Random();
        int nVertex = rand.nextInt(maxVertex) + 1; // number of vertex
        int startVertex = rand.nextInt(nVertex);
        int goalVertex = rand.nextInt(nVertex);
        int nEdge = 0;
        final int MAX_EDGES = nVertex * (nVertex - 1) / 2; // maximum edge in undirected graph

//        Vertex[] vertexes = new Vertex[nVertex];
        double[][] vertexes = new double[nVertex][2];
        double[][] edges = new double[nVertex][nVertex]; // edges[vertex1][vertex2] = weight if edge(vertex1 - vertex2) = weight

//        create vertexes (the number of vertex = nVertex)
        for (int v = 0; v < nVertex; v++) {
            double x = rand.nextDouble() * maxVertex; // randomly generate x coordiate
            double y = rand.nextDouble() * maxVertex; // randomly generate y coordiate
            vertexes[v] = new double[]{x, y};
        }

//        create edges (maximum number of edge = MAX_EDGES)
        for (int e = 0; e < MAX_EDGES; e++) {
//         edges[v1][v2] >= eucleadianDist(v1,v2). v1,v2 in range(0, nVertex)
            int v1 = rand.nextInt(nVertex);
            int v2 = rand.nextInt(nVertex);
            while (v2 == v1) { // remove loop
                v2 = rand.nextInt(nVertex);
            }
            if (edges[v1][v2] == 0 && edges[v2][v1] == 0) {
                edges[v1][v2] = edgeWeight(vertexes[v1], vertexes[v2]);
                nEdge++;
            }
        }

//        generate input
        input.append(String.format("%d\t%d\n", nVertex, nEdge));
        for (int i = 0; i < nVertex; i++) {
            input.append(String.format("%d\t%.2f\t%.2f\n", i + 1, vertexes[i][0], vertexes[i][1]));
        }
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                if (edges[i][j] != 0) {
//                    input += String.format("%d\t%d\t%.2f\t%.2f\n", i+1, j+1, edges[i][j], euclediandist(vertexes[i], vertexes[j]));
                    input.append(String.format("%d\t%d\t%.2f\n", i + 1, j + 1, edges[i][j]));
                }
            }
        }
        input.append(String.format("%d\t%d\n", startVertex, goalVertex));
        return input.toString();
    }


    public String inputLoopGenerator(int maxVertex) { // undirected graph may contain loop
//        maxVertex = maximum number of vertex this graph can have
//        init variable
        StringBuilder input = new StringBuilder();
        Random rand = new Random();
        int nVertex = rand.nextInt(maxVertex) + 1; // number of vertex
        int startVertex = rand.nextInt(nVertex);
        int goalVertex = rand.nextInt(nVertex);
        int nEdge = 0;
        final int MAX_EDGES = nVertex * (nVertex - 1) / 2; // maximum edge in undirected graph

//        Vertex[] vertexes = new Vertex[nVertex];
        double[][] vertexes = new double[nVertex][2];
        double[][] edges = new double[nVertex][nVertex]; // edges[vertex1][vertex2] = weight if edge(vertex1 - vertex2) = weight

//        create vertexes (the number of vertex = nVertex)
        for (int v = 0; v < nVertex; v++) {
            double x = rand.nextDouble() * maxVertex; // randomly generate x coordiate
            double y = rand.nextDouble() * maxVertex; // randomly generate y coordiate
            vertexes[v] = new double[]{x, y};
        }

//        create edges (maximum number of edge = MAX_EDGES)
        for (int e = 0; e < MAX_EDGES; e++) {
//         edges[v1][v2] >= eucleadianDist(v1,v2). v1,v2 in range(0, nVertex)
            int v1 = rand.nextInt(nVertex);
            int v2 = rand.nextInt(nVertex);
            if (edges[v1][v2] == 0 && edges[v2][v1] == 0) {
                edges[v1][v2] = edgeWeight(vertexes[v1], vertexes[v2]);
                nEdge++;
            }
        }
//        generate input
        input.append(String.format("%d\t%d\n", nVertex, nEdge));
        for (int i = 0; i < nVertex; i++) {
            input.append(String.format("%d\t%.2f\t%.2f\n", i + 1, vertexes[i][0], vertexes[i][1]));
        }
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                if (edges[i][j] != 0) {
//                    input += String.format("%d\t%d\t%.2f\t%.2f\n", i+1, j+1, edges[i][j], euclediandist(vertexes[i], vertexes[j]));
                    input.append(String.format("%d\t%d\t%.2f\n", i + 1, j + 1, edges[i][j]));
                }
            }
        }
        input.append(String.format("%d\t%d\n", startVertex, goalVertex));
        return input.toString();
    }


    public double edgeWeight(double[] v1, double[] v2) {
        double deltaX = v1[0] - v2[0];
        double deltaY = v1[1] - v2[1];
        double eucledianDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return Math.random() * (2 * eucledianDistance + 1) + eucledianDistance;
    }
}
