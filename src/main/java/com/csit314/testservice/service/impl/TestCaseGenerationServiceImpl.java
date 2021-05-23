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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class TestCaseGenerationServiceImpl implements TestCaseGenerationService {
    @Autowired
    private TestCaseMapperImpl testCaseMapper;
    @Autowired
    private Judge0ServiceIntegration judge0ServiceIntegration;
    @Autowired
    private RedisTemplate<String, List<CachedTestCase>> testCaseCache;

    /*Executed after the bean is instantiated*/
    @PostConstruct
    public void init() throws InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        testCaseCache.delete(TestCaseType.edgeCase.toString());
        testCaseCache.delete(TestCaseType.shortestPathOnly.toString());
        testCaseCache.delete(TestCaseType.bothShortestAndSecondShortestPath.toString());
        if (!Objects.requireNonNull(testCaseCache.hasKey(TestCaseType.edgeCase.toString()))) {
            addEdgeCaseToCache();
        }
        if (!Objects.requireNonNull(testCaseCache.hasKey(TestCaseType.shortestPathOnly.toString()))) {
            addShortestPathOnlyTestCaseToCache();
        }
        if (!Objects.requireNonNull(testCaseCache.hasKey(TestCaseType.bothShortestAndSecondShortestPath.toString()))) {
            addBothShortestAndSecondShortestPathTestCaseToCache();
        }
    }

    public TestCaseGenerationServiceImpl() {

    }

    @Override
    public List<CachedTestCase> getTestCaseByTypes(List<TestCaseType> testCaseTypes) {
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        List<CachedTestCase> testCases = new ArrayList<>();
        for (TestCaseType type : testCaseTypes) {
            testCases.addAll(Objects.requireNonNull(operations.get(type.toString())));
        }
        return testCases;
    }


    /*Helper method to generate test case*/
    private List<CachedTestCase> generateTestCase(Method method, TestCaseSize size, TestCaseType type, int quantity) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        SubmissionBatchRequestDto submissionBatchRequestDto = new SubmissionBatchRequestDto();
        for (int i = 0; i < quantity; i++) {
            String stdin = (String) method.invoke(new TestCaseGenerationServiceImpl());
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
    private void addShortestPathOnlyTestCaseToCache() throws NoSuchMethodException, InterruptedException, IllegalAccessException, InvocationTargetException {
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        List<CachedTestCase> testCases = new ArrayList<>();
        testCases.addAll(generateTestCase(TestCaseGenerationServiceImpl.class.getMethod("shortestPathOnlyLargeInputGenerator"), TestCaseSize.Large, TestCaseType.shortestPathOnly, 10));
        testCases.addAll(generateTestCase(TestCaseGenerationServiceImpl.class.getMethod("shortestPathOnlyMediumInputGenerator"), TestCaseSize.Medium, TestCaseType.shortestPathOnly, 15));
        testCases.addAll(generateTestCase(TestCaseGenerationServiceImpl.class.getMethod("shortestPathOnlySmallInputGenerator"), TestCaseSize.Small, TestCaseType.shortestPathOnly, 5));
        operations.set(TestCaseType.shortestPathOnly.toString(), testCases);
    }

    private void addBothShortestAndSecondShortestPathTestCaseToCache() throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        List<CachedTestCase> testCases = new ArrayList<>();
        testCases.addAll(generateTestCase(TestCaseGenerationServiceImpl.class.getMethod("bothShortestAndSecondLargeInputGenerator"), TestCaseSize.Large, TestCaseType.bothShortestAndSecondShortestPath, 10));
        testCases.addAll(generateTestCase(TestCaseGenerationServiceImpl.class.getMethod("bothShortestAndSecondMediumInputGenerator"), TestCaseSize.Medium, TestCaseType.bothShortestAndSecondShortestPath, 15));
        testCases.addAll(generateTestCase(TestCaseGenerationServiceImpl.class.getMethod("bothShortestAndSecondSmallInputGenerator"), TestCaseSize.Small, TestCaseType.bothShortestAndSecondShortestPath, 5));
        operations.set(TestCaseType.bothShortestAndSecondShortestPath.toString(), testCases);
    }

    private void addEdgeCaseToCache() throws NoSuchMethodException, InterruptedException, IllegalAccessException, InvocationTargetException {
        final ValueOperations<String, List<CachedTestCase>> operations = testCaseCache.opsForValue();
        List<CachedTestCase> testCases = new ArrayList<>();
        testCases.addAll(generateTestCase(TestCaseGenerationServiceImpl.class.getMethod("edgeCaseInputGenerator"), TestCaseSize.Small, TestCaseType.edgeCase, 5));
        operations.set(TestCaseType.edgeCase.toString(), testCases);
    }

    public String bothShortestAndSecondMediumInputGenerator() {
        return inputGenerator(70);
    }

    public String bothShortestAndSecondLargeInputGenerator() {
        return inputGenerator(310);
    }

    public String bothShortestAndSecondSmallInputGenerator() {
        return inputGenerator(30);
    }


    public String shortestPathOnlyMediumInputGenerator() {
        return shortestPathOnlyInputGenerator(70);
    }

    public String shortestPathOnlyLargeInputGenerator() {
        return shortestPathOnlyInputGenerator(310);
    }

    public String shortestPathOnlySmallInputGenerator() {
        return shortestPathOnlyInputGenerator(30);
    }


    public String edgeCaseInputGenerator() {
        final int MAX_VERTEX = 1;
        String input = "";
        Random rand = new Random();
        int nVertex = rand.nextInt(MAX_VERTEX+1);// generate nVertex in range 0 (inclusive) to 1 (inclusive)
        switch (nVertex) {
            case 0:
                input =  "0 0\n0 0";
                break;
            case 1:
                input = "1 0\n1 3.4 8.5\n1 1";
                break;
        }

        return input;
    }

    public String inputGenerator(int maxVertex) { // undirected graph with no loop, startVertex differs from goalVertex
//        maxVertex = maximum number of vertex this graph can have
//        init variable
        StringBuilder input = new StringBuilder();
        Random rand = new Random();
        int nVertex = rand.nextInt(maxVertex) + 1; // number of vertex
        int startVertex = rand.nextInt(nVertex);
//        ensure goalVertex differs from startVertex
        int goalVertex = rand.nextInt(nVertex);
        while(goalVertex == startVertex) {
            goalVertex = rand.nextInt(nVertex);
        }
        int nEdge = 0;
        final int MAX_EDGES = nVertex * (nVertex - 1) / 2; // maximum edge in undirected graph

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
//        ensure goalVertex differs from startVertex
        int goalVertex = rand.nextInt(nVertex);
        while(goalVertex == startVertex) {
            goalVertex = rand.nextInt(nVertex);
        }
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

    public String shortestPathOnlyInputGenerator(int maxVertex) { // generate input with only shortest path, no second shortest path
//       generate a disconnected graph where startVertex and goalVertex are isolated from the rest of graph. Hence there is only 1 path between startVertex and goalVertex- this is also the shortest path between them, no second shortest path

//        maxVertex = maximum number of vertex this graph can have
//        init variable
        StringBuilder input = new StringBuilder();
        Random rand = new Random();
        int nVertex = rand.nextInt(maxVertex) + 4; // number of vertex, min = 4
        int startVertex = rand.nextInt(nVertex);
//        ensure goalVertex differs from startVertex
        int goalVertex;
        do {
            goalVertex = rand.nextInt(nVertex);
        } while(goalVertex == startVertex);

        int nEdge = 0;
        final int MAX_EDGES = nVertex * (nVertex - 1) / 2; // maximum edge in undirected graph

        double[][] vertexes = new double[nVertex][2];
        double[][] edges = new double[nVertex][nVertex]; // edges[vertex1][vertex2] = weight if edge(vertex1 - vertex2) = weight

//        create vertexes (the number of vertex = nVertex)
        for (int v = 0; v < nVertex; v++) {
            double x = rand.nextDouble() * maxVertex; // randomly generate x coordiate
            double y = rand.nextDouble() * maxVertex; // randomly generate y coordiate
            vertexes[v] = new double[]{x, y};
        }

//        create edges (maximum number of edge = MAX_EDGES)
        edges[startVertex][goalVertex] = edgeWeight(vertexes[startVertex], vertexes[goalVertex]);
        for (int e = 0; e < MAX_EDGES; e++) {
            int v1, v2;
//            make sure there are no edges link to start or goalVertex
            do {
                v1 = rand.nextInt(nVertex);
            } while (v1 == goalVertex || v1 == startVertex);

            do {
                v2 = rand.nextInt(nVertex);
            } while (v2 == goalVertex || v2 == startVertex || v2 == v1) ;

            if (edges[v1][v2] == 0 && edges[v2][v1] == 0) {
                edges[v1][v2] = edgeWeight(vertexes[v1], vertexes[v2]);
                nEdge++;
            }
        }

//        GENERATE INPUT
//        1st line: nVertex nEdge
        input.append(String.format("%d\t%d\n", nVertex, nEdge+1));

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
