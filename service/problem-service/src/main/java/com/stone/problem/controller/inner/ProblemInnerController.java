package com.stone.problem.controller.inner;

import com.stone.model.entity.Problem;
import com.stone.model.entity.ProblemSubmit;
import com.stone.problem.service.ProblemService;
import com.stone.problem.service.ProblemSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class ProblemInnerController {

    @Resource
    private ProblemService problemService;

    @Resource
    private ProblemSubmitService problemSubmitService;

    @GetMapping("/get/id")
    public Problem getProblemById(@RequestParam("problemId") long problemId) {
        return problemService.getById(problemId);
    }

    @GetMapping("/problem_submit/get/id")
    public ProblemSubmit getProblemSubmitById(@RequestParam("problemId") long problemSubmitId) {
        return problemSubmitService.getById(problemSubmitId);
    }

    @PostMapping("/problem_submit/update")
    public boolean updateProblemSubmitById(@RequestBody ProblemSubmit problemSubmit) {
        return problemSubmitService.updateById(problemSubmit);
    }

}
