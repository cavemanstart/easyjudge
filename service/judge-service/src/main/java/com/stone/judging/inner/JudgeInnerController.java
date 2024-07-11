package com.stone.judging.inner;
import com.stone.judging.judge.JudgeService;
import com.stone.model.entity.ProblemSubmit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController{
    @Resource
    private JudgeService judgeService;

    /**
     * 判题
     * @param problemSubmitId
     * @return
     */
    @PostMapping("/do")
    public ProblemSubmit doJudge(@RequestParam("problemSubmitId") long problemSubmitId){
        return judgeService.doJudge(problemSubmitId);
    }
}
