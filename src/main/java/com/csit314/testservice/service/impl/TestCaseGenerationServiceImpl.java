package com.csit314.testservice.service.impl;

import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.entity.enums.TestCaseSize;
import com.csit314.testservice.entity.enums.TestCaseType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TestCaseGenerationServiceImpl implements TestCaseGenerationService {
    private final TestCaseMapperImpl testCaseMapper;
    private final Judge0ServiceIntegration judge0ServiceIntegration;
    private final RedisTemplate<String, List<CachedTestCase>> testCaseCache;

    /*Executed after the bean is instantiated*/
    @PostConstruct
    public void init() throws InterruptedException {
        if(!Objects.requireNonNull(testCaseCache.hasKey(TestCaseType.EdgeCase.toString()))){
            addEdgeCaseToCache();
        }
        if(!Objects.requireNonNull(testCaseCache.hasKey(TestCaseType.ShortestPathOnly.toString()))){
            addShortestPathOnlyTestCaseToCache();
        }
        if(!Objects.requireNonNull(testCaseCache.hasKey(TestCaseType.BothShortestAndSecondShortestPath.toString()))){
            addBothShortestAndSecondShortestPathTestCaseToCache();
        }
    }

    @Override
    public List<CachedTestCase> getTestCaseByTypes(List<TestCaseType> testCaseTypes) {
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        List<CachedTestCase> testCases = new ArrayList<>();
        for(TestCaseType type : testCaseTypes){
            testCases.addAll(Objects.requireNonNull(operations.get(type.toString())));
        }
        return testCases;
    }


    /*Helper method to generate test case*/
    private List<CachedTestCase> generateTestCase(String stdin, TestCaseSize size, TestCaseType type, int quantity) throws InterruptedException {
        SubmissionBatchRequestDto submissionBatchRequestDto = new SubmissionBatchRequestDto();
        for(int i = 0; i < quantity;i++){
            submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(stdin).language_id(52).build());
        }
        SubmissionBatchResponseDto submissionBatchResponseDto = judge0ServiceIntegration.getExpectedOutputBatch(submissionBatchRequestDto);
        List<CachedTestCase> testCases = testCaseMapper.fromSubmissionBatchResponseToCachedTestCase(submissionBatchResponseDto);
        for (CachedTestCase testCase : testCases) {
            testCase.setSize(size);
            testCase.setType(type);
        }
        return testCases;
    }

    /*Insert to redis database */
    private void addShortestPathOnlyTestCaseToCache(){
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        operations.set(TestCaseType.ShortestPathOnly.toString(), new ArrayList<>());
    }

    private void addBothShortestAndSecondShortestPathTestCaseToCache() throws InterruptedException {
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        List<CachedTestCase> testCases = new ArrayList<>();
        testCases.addAll(generateTestCase(bigInputGenerator(),TestCaseSize.Big,TestCaseType.BothShortestAndSecondShortestPath,10));
        testCases.addAll(generateTestCase(mediumInputGenerator(),TestCaseSize.Medium,TestCaseType.BothShortestAndSecondShortestPath,15));
        testCases.addAll(generateTestCase(smallInputGenerator(),TestCaseSize.Small,TestCaseType.BothShortestAndSecondShortestPath,5));
        operations.set(TestCaseType.BothShortestAndSecondShortestPath.toString(), testCases);
    }
    private void addEdgeCaseToCache(){
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        operations.set(TestCaseType.EdgeCase.toString(), new ArrayList<>());
    }


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

//        GENERATE INPUT
//        1st line: nVertex nEdge
        input.append(String.format("%d\t%d\n", nVertex, nEdge));

//        vertex lines: vertex xcoordinate ycoordinate
        for (int i = 0; i < nVertex; i++) {
            input.append(String.format("%d\t%.2f\t%.2f\n", i + 1, vertexes[i][0], vertexes[i][1]));
        }

//        edge lines: vertex vertex weight
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                if (edges[i][j] != 0) {
                    input.append(String.format("%d\t%d\t%.2f\n", i + 1, j + 1, edges[i][j]));
                }
            }
        }

//        last line: startVertex goalVertex
        input.append(String.format("%d\t%d\n", startVertex + 1, goalVertex + 1));
        return input.toString();
    }


    public String inputLoopGenerator(int maxVertex) { // undirected graph may contain loop
//        maxVertex = maximum number of vertex this graph can have
//        INIT VARIABLE
        StringBuilder input = new StringBuilder();
        Random rand = new Random();
        int nVertex = rand.nextInt(maxVertex) + 1; // number of vertex
        int startVertex = rand.nextInt(nVertex);
        int goalVertex = rand.nextInt(nVertex);
        int nEdge = 0;
        final int MAX_EDGES = nVertex * (nVertex - 1) / 2; // maximum edge in undirected graph
        double[][] vertexes = new double[nVertex][2];
        double[][] edges = new double[nVertex][nVertex]; // edges[vertex1][vertex2] = weight if edge(vertex1 - vertex2) = weight

//        CREATE VERTEX AND EDGE
//        create vertexes (the number of vertex = nVertex)
        for (int v = 0; v < nVertex; v++) {
            double x = rand.nextDouble() * maxVertex; // randomly generate x coordiate
            double y = rand.nextDouble() * maxVertex; // randomly generate y coordiate
            vertexes[v] = new double[]{x, y};
        }

//        create edges (maximum number of edge = MAX_EDGES)
        for (int e = 0; e < MAX_EDGES; e++) {
            int v1 = rand.nextInt(nVertex);
            int v2 = rand.nextInt(nVertex);
            if (edges[v1][v2] == 0 && edges[v2][v1] == 0) {
                edges[v1][v2] = edgeWeight(vertexes[v1], vertexes[v2]);
                nEdge++;
            }
        }
//        GENERATE INPUT
//        1st line: nVertex nEdge
        input.append(String.format("%d\t%d\n", nVertex, nEdge));

//        vertex lines: vertex xcoordinate ycoordinate
        for (int i = 0; i < nVertex; i++) {
            input.append(String.format("%d\t%.2f\t%.2f\n", i + 1, vertexes[i][0], vertexes[i][1]));
        }

//        edge lines: vertex vertex weight
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                if (edges[i][j] != 0) {
                    input.append(String.format("%d\t%d\t%.2f\n", i + 1, j + 1, edges[i][j]));
                }
            }
        }

//        last line: startVertex goalVertex
        input.append(String.format("%d\t%d\n", startVertex + 1, goalVertex + 1));
        return input.toString();
    }


    public double edgeWeight(double[] v1, double[] v2) {
        double deltaX = v1[0] - v2[0];
        double deltaY = v1[1] - v2[1];
        double eucledianDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return Math.random() * (2 * eucledianDistance + 1) + eucledianDistance;
    }


}
