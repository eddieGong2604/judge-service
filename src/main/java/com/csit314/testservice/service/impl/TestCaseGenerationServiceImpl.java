package com.csit314.testservice.service.impl;

import com.csit314.testservice.config.CachedAssignment;
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
    public List<TestCaseResponseDto> generateTestCase() throws InterruptedException {
        final ValueOperations<String, CachedAssignment> operations = assignmentCache.opsForValue();
        if (!assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY)) {
            operations.set(ASSIGNMENT_CACHE_KEY, CachedAssignment.builder().assignmentName(ASSIGNMENT_CACHE_KEY).code("#include <iostream> \n using namespace std; \n int main(){int a;cin >> a;cout<< a;return 0;}").build());
        }
        if (assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY) && (operations.get(ASSIGNMENT_CACHE_KEY)).getCachedTestCases() != null) {
            return testCaseMapper.fromCachedToResponseTestCase(Objects.requireNonNull(operations.get(ASSIGNMENT_CACHE_KEY)).getCachedTestCases());
        }
        SubmissionBatchRequestDto submissionBatchRequestDto = new SubmissionBatchRequestDto();
        /*Generate 1 medium test cases*/
        submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(SourceCodeConstants.CORRECT_SOURCE_CODE).stdin(inputGenerator()).language_id(52).build());

        SubmissionBatchResponseDto submissionBatchResponseDto = judge0ServiceIntegration.getExpectedOutputBatch(submissionBatchRequestDto);
        CachedAssignment updatedCachedAssignment = CachedAssignment.builder().cachedTestCases(testCaseMapper.fromSubmissionBatchResponseToCachedTestCase(submissionBatchResponseDto)).assignmentName(ASSIGNMENT_CACHE_KEY).code(SourceCodeConstants.CORRECT_SOURCE_CODE).build();

        assignmentCache.delete(ASSIGNMENT_CACHE_KEY);
        operations.set(ASSIGNMENT_CACHE_KEY, updatedCachedAssignment);
        return testCaseMapper.fromSubmissionBatchResponseToTestcaseResponseDto(submissionBatchResponseDto);
    }
    /*TODO: Gen 5 medium Input, 4 small inputs, and 5 big input, 1 edge case*/
    private String inputGenerator() {
        return "20\t100\t\n" +
                "1\t6\t25\n" +
                "2\t12\t66\n" +
                "3\t16\t23\n" +
                "4\t21\t24\n" +
                "5\t24\t39\n" +
                "6\t32\t27\n" +
                "7\t36\t98\n" +
                "8\t40\t67\n" +
                "9\t52\t53\n" +
                "10\t54\t4\n" +
                "11\t76\t16\n" +
                "12\t81\t64\n" +
                "13\t83\t98\n" +
                "14\t85\t96\n" +
                "15\t88\t81\n" +
                "16\t91\t73\n" +
                "17\t94\t78\n" +
                "18\t95\t29\n" +
                "19\t98\t80\n" +
                "20\t99\t84\n" +
                "1\t3\t19\n" +
                "1\t6\t34\n" +
                "1\t9\t58\n" +
                "1\t12\t94\n" +
                "1\t15\t108\n" +
                "1\t16\t104\n" +
                "2\t1\t46\n" +
                "2\t4\t47\n" +
                "2\t5\t36\n" +
                "2\t10\t82\n" +
                "2\t13\t85\n" +
                "2\t16\t88\n" +
                "2\t17\t91\n" +
                "2\t18\t96\n" +
                "3\t8\t57\n" +
                "3\t10\t49\n" +
                "3\t12\t84\n" +
                "3\t13\t106\n" +
                "4\t10\t44\n" +
                "4\t12\t82\n" +
                "4\t18\t80\n" +
                "5\t4\t22\n" +
                "5\t17\t88\n" +
                "5\t18\t79\n" +
                "5\t20\t93\n" +
                "6\t2\t53\n" +
                "6\t4\t19\n" +
                "6\t19\t93\n" +
                "7\t8\t36\n" +
                "7\t10\t102\n" +
                "7\t15\t64\n" +
                "7\t16\t65\n" +
                "8\t1\t60\n" +
                "8\t4\t56\n" +
                "8\t6\t46\n" +
                "8\t9\t26\n" +
                "8\t12\t47\n" +
                "8\t19\t64\n" +
                "9\t3\t56\n" +
                "9\t4\t48\n" +
                "9\t8\t28\n" +
                "9\t16\t50\n" +
                "10\t3\t51\n" +
                "10\t9\t58\n" +
                "10\t13\t108\n" +
                "11\t3\t68\n" +
                "11\t7\t96\n" +
                "11\t9\t51\n" +
                "11\t10\t34\n" +
                "11\t12\t55\n" +
                "11\t19\t77\n" +
                "11\t20\t77\n" +
                "12\t2\t76\n" +
                "12\t3\t84\n" +
                "12\t5\t69\n" +
                "12\t7\t64\n" +
                "12\t10\t73\n" +
                "12\t11\t58\n" +
                "12\t17\t25\n" +
                "12\t18\t44\n" +
                "12\t20\t36\n" +
                "13\t6\t93\n" +
                "13\t9\t63\n" +
                "14\t8\t61\n" +
                "14\t9\t64\n" +
                "14\t12\t40\n" +
                "14\t16\t28\n" +
                "14\t17\t29\n" +
                "15\t4\t95\n" +
                "15\t5\t84\n" +
                "15\t11\t71\n" +
                "15\t17\t16\n" +
                "16\t2\t85\n" +
                "16\t4\t90\n" +
                "16\t7\t67\n" +
                "16\t8\t57\n" +
                "16\t10\t84\n" +
                "16\t15\t17\n" +
                "17\t9\t58\n" +
                "17\t14\t28\n" +
                "17\t15\t15\n" +
                "17\t19\t11\n" +
                "17\t20\t12\n" +
                "18\t5\t78\n" +
                "18\t10\t57\n" +
                "18\t12\t42\n" +
                "18\t14\t72\n" +
                "18\t19\t61\n" +
                "18\t20\t63\n" +
                "19\t3\t104\n" +
                "19\t4\t105\n" +
                "19\t5\t90\n" +
                "19\t15\t16\n" +
                "19\t17\t10\n" +
                "20\t1\t119\n" +
                "20\t6\t94\n" +
                "20\t9\t64\n" +
                "20\t12\t36\n" +
                "20\t13\t31\n" +
                "20\t14\t28\n" +
                "2\t13\n";
    }

}
