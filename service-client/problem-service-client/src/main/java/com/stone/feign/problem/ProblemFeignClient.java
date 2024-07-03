package com.stone.feign.problem;

import com.stone.model.entity.Problem;
import com.stone.model.entity.ProblemSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @description 针对表【problem(题目)】的数据库操作Service
* @createDate 2023-08-07 20:58:00
*/
@FeignClient(name = "problem-service")
public interface ProblemFeignClient {

    @GetMapping("/get/id")
    Problem getProblemById(@RequestParam("problemId") long problemId);

    @GetMapping("/problem_submit/get/id")
    ProblemSubmit getProblemSubmitById(@RequestParam("problemId") long problemSubmitId);

    @PostMapping("/problem_submit/update")
    boolean updateProblemSubmitById(@RequestBody ProblemSubmit problemSubmit);

}
