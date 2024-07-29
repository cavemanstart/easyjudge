package com.stone.judging.controller.inner;
import com.stone.judging.judge.JudgeService;
import com.stone.model.entity.QuestionSubmit;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController{
    @Resource
    private JudgeService judgeService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        QuestionSubmit questionSubmit = null;
        CompletableFuture<QuestionSubmit> completableFuture = CompletableFuture.supplyAsync(
                ()-> judgeService.doJudge(questionSubmitId), threadPoolTaskExecutor
        );
        try {
            questionSubmit = completableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionSubmit;
    }
}
